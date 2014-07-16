/*
 * FileUtils
 * 
 * 0.1
 * 
 * 2014/07/16
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

package r2b.apps.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * File utility helper.
 */
public class FileUtils {
	
	/**
	 * Check if external storage is ready to read and write.
	 * @return True is ready, false otherwise.
	 */
	public static boolean isExternalStorageReady() {
		
		final String externalStorageState = Environment.getExternalStorageState();
		boolean isStorageReady = false;
		
		if ( Environment.MEDIA_MOUNTED.equals( externalStorageState ) ) {
		    // We can read and write the media
			isStorageReady = true;
		} 
		else {
			Log.i(FileUtils.class.getSimpleName(), "External storage not ready to save logs.");
		}
		
		return isStorageReady;
		
	}
	
	/**
	 * Open or create a file on external storage.
	 * @param context Application context
	 * @param dirName Directory name
	 * @param fileName File name (with extension)
	 * @return The created file, or the opened file if exist previously.
	 */
	public static File createExternalStorageFile(Context context, String dirName, String fileName) {
	    	    
	    // Create a path where it will place the file on external storage
	    File sdCard = Environment.getExternalStorageDirectory();  
	    File root = new File (sdCard.getAbsolutePath() + File.separator + dirName);  
		if(!root.exists()) {
			root.mkdirs();
		}
		
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(root, fileName);	   
	    if(!file.exists()) {
	    	try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(FileUtils.class.getSimpleName(), e.toString());
			}
	    }
	    
	    return file;
	    
	}
	
	/**
	 * Open or create a file on internal storage.
	 * @param context Application context
	 * @param fileName File name (with extension)
	 * @return The created file, or the opened file if exist previously.
	 */
	public static File createInternalStorageFile(Context context, String fileName) {
		File file = new File(context.getFilesDir(), fileName);
		return file;
	}

}
