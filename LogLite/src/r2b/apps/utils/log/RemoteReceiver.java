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
import java.io.FileInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;

public class RemoteReceiver {
	
	private FileReceiver receiver;
	
	/**
	 * Log file extension.
	 */
	private static final String FILE_EXTENSION = ".log";
	/**
	 * Default dir name.
	 */
	private static final String DEFAULT_DIRECTORY_NAME = "LogLite";
	
	/**
	 * Worker thread
	 */
	private final Thread worker = new Thread() {
		@Override
		public void run() {													
			
		}
	};

	public RemoteReceiver(final Context context) {				
		receiver = new FileReceiver(context);
	}

	public void close() {
		receiver.close();
	}
	
	public void v(String msg) {
		receiver.v(msg);
	}
	
	public void d(String msg) {
		receiver.d(msg);
	}		
	
	public void i(String msg) {
		receiver.i(msg);
	}
	
	public void e(String msg) {
		receiver.e(msg);
	}
	
	private void send() {
		
		String url = "http://yourserver";
		
		File file = new File(Environment.getExternalStorageDirectory(), "yourfile");
		
		try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);

		    MultipartEntity entity = new MultipartEntity();

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/Test.jpg");
            entity.addPart("picture", new FileBody(file));

            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
		    //Do something with response...

		} catch (Exception e) {
		    // show error
		}
	}
	
}
