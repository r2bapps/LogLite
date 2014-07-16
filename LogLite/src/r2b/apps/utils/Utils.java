package r2b.apps.utils;

import android.content.Context;

public final class Utils {

	public static String getApplicationName(Context context) {
		
		int stringId = context.getApplicationInfo().labelRes;
	    String appName = context.getString(stringId);
	    
	    return appName;	    
	}
	
}
