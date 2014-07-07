package r2b.apps.utils.log.test;

import r2b.apps.utils.log.Logger;
import r2b.apps.utils.log.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
			
			Thread t = new Thread() {
				@Override
				public void run() {
					Logger.init(getActivity());
					
					for(int i = 0; i < 1000; i++) {
						Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
					}
					
					Logger.close();
				}
			};
			t.start();

		}
		
		
	}

}
