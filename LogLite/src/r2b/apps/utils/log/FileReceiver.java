/*
 * FileReceiver
 * 
 * 0.2
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

import r2b.apps.utils.FileUtils;
import r2b.apps.utils.StringUtils;
import r2b.apps.utils.Utils;
import android.content.Context;
import android.util.Log;

/**
 * Buffered and thread-safe file logger.
 * 
 * Store all logs on a directory called like the app and 
 * a file called like app with .log extension, inside 
 * it folder.
 */
public class FileReceiver implements Receiver {
	
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
	 * Current opened file.
	 */
	private File currentFile;
	/**
	 * File name.
	 */
	private String fileName;
	
	/**
	 * Worker thread
	 */
	private final Thread worker = new Thread() {
		@Override
		public void run() {					
			
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
	};

	public FileReceiver(final Context context) {				
		this.context = context.getApplicationContext();
		if(init()) {
			Log.d(this.getClass().getSimpleName(), "Initialized");
			worker.start();
		}
	}
	
	public FileReceiver(final Context context, String fileName) {				
		this.context = context.getApplicationContext();
		this.fileName = fileName;
		if(init()) {
			Log.d(this.getClass().getSimpleName(), "Initialized");
			worker.start();
		}
	}

	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#close()
	 */
	public void close() {
		if(initialized) {
			exit = true;
			synchronized (stick) {
			    stick.notify();
			}
			initialized = false;
			
			Log.d(this.getClass().getSimpleName(), "Closed");
		}
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#v(java.lang.String)
	 */
	public void v(String msg) {
		print(msg);
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#d(java.lang.String)
	 */
	public void d(String msg) {
		print(msg);
	}		
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#i(java.lang.String)
	 */
	public void i(String msg) {
		print(msg);
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#e(java.lang.String)
	 */
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
		
		if(fileName == null) {
			fileName = Utils.getApplicationName(context);
		    if(fileName == null) {
		    	fileName = DEFAULT_DIRECTORY_NAME;
		    }
		    else {
		    	fileName = StringUtils.
		    			replaceAllWithespacesAndNonVisibleCharacteres(fileName);	    	
		    }
		    
		    fileName += FILE_EXTENSION;
		}
		
		currentFile = FileUtils.createInternalStorageFile(context, fileName);
		
		if( setupPrinter() ) {			
			buffer = new StringBuilder();		
			buffer.setLength(0);
			stick = new Object();
			
			initialized = true;				
		}
		else if( FileUtils.isExternalStorageReady() ) {
			
			String dirName = Utils.getApplicationName(context);
		    if(dirName == null) {
		    	dirName = DEFAULT_DIRECTORY_NAME;
		    }
		    else {
		    	dirName = StringUtils.
		    			replaceAllWithespacesAndNonVisibleCharacteres(dirName);	    	
		    }
		    
			currentFile = FileUtils.
					createExternalStorageFile(
							context, 
							dirName, 
							fileName);	
			
			if( setupPrinter() ) {
				buffer = new StringBuilder();		
				buffer.setLength(0);
				stick = new Object();
				
				initialized = true;				
			}
				
		}		
		else {
			context = null;			
		}
				
		return initialized;
		
	}
	
	private boolean setupPrinter() {
		boolean setup = false;
		
		if( currentFile != null) {
			try {
				FileWriter fw = new FileWriter (currentFile, true);
				BufferedWriter bw = new BufferedWriter (fw);
				printer = new PrintWriter( bw );
				setup = true;
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			}
		}
		
		return setup;
	}
	
	final File getCurrentFile() {
		return this.currentFile;
	}
	
}
