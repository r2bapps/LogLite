/*
 * RemoteReceiver
 * 
 * 0.1
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
import r2b.apps.utils.StringUtils;
import r2b.apps.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class RemoteReceiver {
	
	/**
	 * Default charset.
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";
	/**
	 * Log file extension.
	 */
	private static final String FILE_EXTENSION = ".log";
	/**
	 * Default file name.
	 */
	private static final String DEFAULT_FILE_NAME = "LogLite";
	/**
	 *  File receiver.
	 */
	private FileReceiver fileReceiver; 
	/**
	 * Server URL.
	 */
	private String requestURL;
	
	@SuppressLint("SimpleDateFormat") 
	public RemoteReceiver(Context context, String requestURL) {
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
	    
		fileReceiver = new FileReceiver(context, fileName + timestamp + FILE_EXTENSION);
	}

	
	public void send() {		
			
		fileReceiver.close();
		
        File uploadFile = new File(fileReceiver.getCurrentFileAbsolutePath());
		
        try {
            MultipartUtility multipart = 
            		new MultipartUtility(this.requestURL, DEFAULT_CHARSET);
             
            multipart.addFilePart("fileUpload", uploadFile);
 
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
            
        } catch (final IOException e) {
        	Log.e(RemoteReceiver.class.getSimpleName(), e.toString());
        }
	        	        
	}
	
}
