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
import java.util.List;

import r2b.apps.utils.log.test.MainActivity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class RemoteReceiver {
	
	/**
	 * Log file extension.
	 */
	private static final String FILE_EXTENSION = ".log";
	/**
	 * Default dir name.
	 */
	private static final String DEFAULT_DIRECTORY_NAME = "LogLite";
	
	
	public static void send(final Context context) {
		

			

		int stringId = context.getApplicationInfo().labelRes;
	    String appName = context.getString(stringId);
	    if(appName == null) {
	    	appName = DEFAULT_DIRECTORY_NAME;
	    }
	    else {
	    	appName = appName.replaceAll("\\s+",""); // Replace whitespaces and non visible characteres	    	
	    }
	    File sdCard = Environment.getExternalStorageDirectory();  
	    File root = new File (sdCard.getAbsolutePath() + File.separator + appName);  
			
			
			String charset = "UTF-8";
	        final File uploadFile1 = new File(root, appName + FILE_EXTENSION);
	        String requestURL = "http://192.168.0.194:8080/LogLiteUploadServer/UploadDownloadFileServlet";
	 
	        
			((MainActivity)context).runOnUiThread(new Runnable() {
		         public void run() {
		          	Toast.makeText(context, "File: " + uploadFile1.getAbsolutePath(), Toast.LENGTH_LONG).show();
		         }
				});	
			
	        try {
	            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
	             
//	            multipart.addHeaderField("User-Agent", "CodeJava");
//	            multipart.addHeaderField("Test-Header", "Header-Value");
	             
//	            multipart.addFormField("description", "Cool Pictures");
//	            multipart.addFormField("keywords", "Java,upload,Spring");
	             
	            multipart.addFilePart("fileUpload", uploadFile1);
	 
	            List<String> response = multipart.finish();
	             
	            Log.i(RemoteReceiver.class.getSimpleName(), "SERVER REPLIED:");
	            
	            final StringBuilder buffer= new StringBuilder();
	            for (String line : response) {
	                buffer.append(line);
	            }
	            
	            Log.i(RemoteReceiver.class.getSimpleName(), buffer.toString());
	            
	    		((MainActivity)context).runOnUiThread(new Runnable() {
	    	         public void run() {
	    	          	Toast.makeText(context, "SERVER REPLIED: " + buffer.toString() , Toast.LENGTH_LONG).show();
	    	         }
	    			});	
	            
	        } catch (final IOException e) {
	    		((MainActivity)context).runOnUiThread(new Runnable() {
	    	         public void run() {
	    	          	Toast.makeText(context, "Error: " + e.toString() , Toast.LENGTH_LONG).show();
	    	         }
	    			});	
	        	Log.e(RemoteReceiver.class.getSimpleName(), e.toString());
	        }
	        	
        
	}
	
}
