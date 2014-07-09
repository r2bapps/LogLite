package r2b.apps.utils.log.test;

import r2b.apps.utils.log.Logger;
import r2b.apps.utils.log.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	final static int SIZE = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	

	
	@Override
	protected void onDestroy() {
		Logger.close();
		
		super.onDestroy();
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
		
		Thread t = new Thread() {
			@Override
			public void run() {
														
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Start", Toast.LENGTH_LONG).show();
                    }
				});
				
				final long init = System.currentTimeMillis();
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				final long end = System.currentTimeMillis();
								
				
				// size[100000] 61600
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop(t1): " + String.valueOf((end-init)), Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t2", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t3 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t3", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t4 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t4", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t5 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t5", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t6 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t6", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};
		Thread t7 = new Thread() {
			@Override
			public void run() {
				
				for(int i = 0; i < SIZE; i++) {
					Logger.i(this.getClass().getSimpleName(), String.valueOf(i));
				}
				
				getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    	Toast.makeText(getActivity(), "Stop t7", Toast.LENGTH_LONG).show();
                    }
				});
			}
		};		
		@Override
		public void onResume() {
			super.onResume();
			
			Logger.init(getActivity());
			
			t.start();
						
			t2.start();			

			t3.start();		

			t4.start();
			
			t5.start();			
			
			t6.start();			
			
			t7.start();			

		}
		
		
	}

}
