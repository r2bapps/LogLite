/*
 * FileReceiver
 * 
 * 0.1
 * 
 * 2014/07/05
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.utils.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Buffered and thread-safe file logger.
 */
public class FileReceiver {
	
	/**
	 * Log file extension.
	 */
	private static final String FILE_EXTENSION = ".log";
	/**
	 * Default dir name.
	 */
	private static final String DEFAULT_DIRECTORY_NAME = "LogLite";
	
	/**
	 * Thread stick.
	 */
	private Object stick;
	/**
	 * Log printer.
	 */
	private PrintWriter printer;
	/**
	 * Application context
	 */
	private Context context;	
	/**
	 * Flag to close the thread.
	 */
	private boolean exit;
	/**
	 * Shared buffer between threads.
	 */
	private StringBuilder buffer;
	/**
	 * Initialized flag.
	 */
	private boolean initialized;
	
	/**
	 * Worker thread
	 */
	private final Thread worker = new Thread() {
		@Override
		public void run() {
			
			if(init()) {
			
				while(!exit) {
					
					while(buffer.length() == 0 && !exit) {
						try {
							
							synchronized (stick) {
								stick.wait();
							}							
							
						} catch (InterruptedException e) {
							Log.e(this.getClass().getSimpleName(), e.toString());
						}
					}
					
					String bf = popBuffer();
					printer.write(bf);  					
					
				}
				
				// End
				printer.flush();
				printer.close();
				context = null;				
				
			}
			
		}
	};

	public FileReceiver(final Context context) {				
		this.context = context.getApplicationContext();
		worker.start();		
	}

	public void close() {
		if(initialized) {
			exit = true;
			synchronized (stick) {
			    stick.notify();
			}
			initialized = false;	
		}
	}
	
	public void v(String msg) {
		print(msg);
	}
	
	public void d(String msg) {
		print(msg);
	}		
	
	public void i(String msg) {
		print(msg);
	}
	
	public void e(String msg) {
		print(msg);
	}

	private void print(String msg) {
		
		if(initialized) {
			pushBuffer(msg);
			
			synchronized (stick) {
			    stick.notify();
			}	
		}
		
	}
	
	private synchronized void pushBuffer(String msg) {
		buffer.append(msg);
	}
	
	private synchronized String popBuffer() {
		String bf = buffer.toString();  	
		buffer.setLength(0);
		return bf;
	}
	
	private boolean init() {
		
		initialized = false;
		
		if( isStorageReady() ) {
			createExternalStorageLogFile();			
			
			buffer = new StringBuilder();		
			buffer.setLength(0);
			stick = new Object();
			
			initialized = true;
		
		}		
		else {
			context = null;			
		}
		
		return initialized;
		
	}
	
	private boolean isStorageReady() {
		
		final String externalStorageState = Environment.getExternalStorageState();
		boolean isStorageReady = false;
		
		if ( Environment.MEDIA_MOUNTED.equals( externalStorageState ) ) {
		    // We can read and write the media
			isStorageReady = true;
		} 
		else {
			Log.i(this.getClass().getSimpleName(), "Storage not ready to save logs.");
		}
		
		return isStorageReady;
		
	}
	
	private void createExternalStorageLogFile() {

		File file = hasExternalStorageLogFile();
		if( file != null) {
			try {
				FileWriter fw = new FileWriter (file, true);
				BufferedWriter bw = new BufferedWriter (fw);
				printer = new PrintWriter( bw );
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			}
		}
	    
	}
	
	private File hasExternalStorageLogFile() {
		
		int stringId = context.getApplicationInfo().labelRes;
	    String appName = context.getString(stringId);
	    if(appName == null) {
	    	appName = DEFAULT_DIRECTORY_NAME;
	    }
	    else {
	    	appName = appName.replaceAll("\\s+",""); // Replace whitespaces and non visible characteres	    	
	    }
	    	    
	    // Create a path where we will place our file on external storage
	    File sdCard = Environment.getExternalStorageDirectory();  
	    File root = new File (sdCard.getAbsolutePath() + File.separator + appName);  
		if(!root.exists()) {
			root.mkdirs();
		}
		
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(root, appName + FILE_EXTENSION);	   
	    if(!file.exists()) {
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			}
	    }
	    
	    return file;
	  
	}
	
}
