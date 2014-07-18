/*
 * RemoteReceiver
 * 
 * 0.2
 * 
 * 2014/07/09
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import r2b.apps.utils.Cons;
import r2b.apps.utils.FileUtils;
import r2b.apps.utils.StringUtils;
import r2b.apps.utils.Utils;
import r2b.apps.utils.ZipUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class RemoteReceiver implements Receiver {
	
	/**
	 * Flag to indicate that must compress the file before send it.
	 */
	private static final boolean compression = true;
	/**
	 * File form field name.
	 */
	private static final String FILE_FORM_FIELD_NAME = "fileUpload";
	/**
	 * Default charset.
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	/**
	 * Log file extension.
	 */
	private static final String FILE_EXTENSION = ".log";
	/**
	 * Log compressed file extension.
	 */
	private static final String ZIP_FILE_EXTENSION = ".zip";	
	/**
	 * Default file name.
	 */
	private static final String DEFAULT_FILE_NAME = "LogLite";
	/**
	 * File receiver.
	 */
	private FileReceiver fileReceiver; 
	/**
	 * External receiver flag.
	 */
	private boolean externalReceiver;
	/**
	 * Server URL.
	 */
	private String requestURL;
	/**
	 * The name of the file to upload.
	 */
	private String fileNameToUpload;
	/**
	 * Initialized flag.
	 */
	private boolean initialized;
	
	@SuppressLint("SimpleDateFormat") 
	public RemoteReceiver(Context context, String requestURL, final FileReceiver fileReceiver) {
		this.requestURL = requestURL;	
		
		String fileName = Utils.getApplicationName(context);
	    if(fileName == null) {
	    	fileName = DEFAULT_FILE_NAME;
	    }
	    else {
	    	fileName = StringUtils.
	    			replaceAllWithespacesAndNonVisibleCharacteres(fileName);	    	
	    }
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss_"); 
	    String timestamp = sdf.format((new Date(System.currentTimeMillis())));
	    
	    final String id = Settings.Secure
        		.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
	    
	    this.fileNameToUpload = fileName + timestamp + id;
	    
	    if(compression) {
	    	this.fileNameToUpload += ZIP_FILE_EXTENSION;
	    }
	    else {
	    	this.fileNameToUpload += FILE_EXTENSION;
	    }
	    
	    if(fileReceiver == null) {
	    	 if(compression) {
	    		 this.fileReceiver = new FileReceiver(context);	
	    	 }
	    	 else {
	    		 this.fileReceiver = new FileReceiver(context, this.fileNameToUpload);	
	    	 }
	    }
	    else {
	    	this.externalReceiver = true;
	    	this.fileReceiver = fileReceiver;
	    }
	    
	    initialized = true;
	    
	    Log.d(this.getClass().getSimpleName(), "Initialized");
		
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#v(java.lang.String)
	 */
	public void v(String msg) {
		if(!this.externalReceiver) {
			fileReceiver.v(msg);
		}
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#d(java.lang.String)
	 */
	public void d(String msg) {
		if(!this.externalReceiver) {
			fileReceiver.d(msg);
		}
	}		
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#i(java.lang.String)
	 */
	public void i(String msg) {
		if(!this.externalReceiver) {
			fileReceiver.i(msg);
		}
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#e(java.lang.String)
	 */
	public void e(String msg) {
		if(!this.externalReceiver) {
			fileReceiver.e(msg);
		}
	}
	
	/* (non-Javadoc)
	 * @see r2b.apps.utils.log.Receiver#close()
	 */
	public void close() {
		if(initialized) {
			fileReceiver.close();
			
			File uploadFile = null;
			
			if(externalReceiver) {
				if(compression) {
					uploadFile = compress(fileReceiver.getCurrentFile().
							getAbsolutePath(), false);
				}
				else {
					File src = fileReceiver.getCurrentFile();
					uploadFile = new File( FileUtils.getFilePath(src) 
								+ File.separator + fileNameToUpload);
					FileUtils.copy(src, uploadFile);
				}
			}
			else {
				if(compression) {
					uploadFile = compress(fileReceiver.getCurrentFile().
							getAbsolutePath(), true);
				}
				else {
					uploadFile = fileReceiver.getCurrentFile();
				}
			}			
			
			send(uploadFile);
        		
			// Delete the copy or the file of the internal receiver
			if(uploadFile.delete()) {    		
				Log.d(this.getClass().getSimpleName(), "Deleted temp file");
			}
			
			initialized = false;	
			
			Log.d(this.getClass().getSimpleName(), "Closed");
		}
	}
	
	private void send(File uploadFile) {		
		      
        try {
            // TODO CHANGE IT
            MultipartUtility multipart = 
            		new MultipartUtility(this.requestURL, DEFAULT_CHARSET);
             
            multipart.addFilePart(FILE_FORM_FIELD_NAME, uploadFile);
 
            // TODO CHANGE IT
            List<String> response = multipart.finish();
            
            if(Cons.DEBUG) {
            	
                final StringBuilder buffer = new StringBuilder();
                
                for (String line : response) {
                    buffer.append(line);
                }
                
                Log.i(RemoteReceiver.class.getSimpleName(), 
                		"Server response: \n" + buffer.toString());
            }
            
        }  
        catch (IOException e) {
        	Log.e(RemoteReceiver.class.getSimpleName(), e.toString());
        } 
	        	        
	}
	
	private File compress(String absolutePath, boolean removeUncompressed) {
		// Compress file on zip and delete them.
		File zip = new File(FileUtils.
				getFilePath(fileReceiver.getCurrentFile()) 
				+ File.separator + fileNameToUpload);
		
		ZipUtils.zip(zip, absolutePath, removeUncompressed);
		
		return zip;
	}
	
}
