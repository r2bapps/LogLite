package r2b.apps.utils.log.test;

import r2b.apps.utils.log.FileReceiver;
import r2b.apps.utils.log.Logger;
import r2b.apps.utils.log.R;
import r2b.apps.utils.log.Receiver;
import r2b.apps.utils.log.RemoteReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	final static int SIZE = 10;
	
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
			((Button)rootView.findViewById(R.id.btn)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Logger.close();
				}
			});
			
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
					Logger.i(this.getClass().getSimpleName(), "Thread t:" + String.valueOf(i));
				}
				
				final long end = System.currentTimeMillis();
								
				
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
					Logger.i(this.getClass().getSimpleName(), "Thread t2:" + String.valueOf(i));
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
					Logger.i(this.getClass().getSimpleName(), "Thread t3:" + String.valueOf(i));
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
					Logger.i(this.getClass().getSimpleName(), "Thread t4:" + String.valueOf(i));
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
					Logger.i(this.getClass().getSimpleName(), "Thread t5:" + String.valueOf(i));
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
					Logger.i(this.getClass().getSimpleName(), "Thread t6:" + String.valueOf(i));
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
					Logger.i(this.getClass().getSimpleName(), "Thread t7:" + String.valueOf(i));
				}
				
				Logger.e(this.getClass().getSimpleName(), "Error");
				
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
			
			String url = "http://192.168.0.195:8080/LogLiteUploadServer/UploadDownloadFileServlet";
			
			FileReceiver fileReceiver = new FileReceiver(getActivity(), null, true);
			FileReceiver fileReceiver2 = new FileReceiver(getActivity(), "Test", true);
			RemoteReceiver remoteReceiver = RemoteReceiver.getInstance(getActivity(), url, true);
					
			
			Receiver [] receivers = new Receiver[3];
			receivers[0] = fileReceiver;
			receivers[1] = fileReceiver2;
			receivers[2] = remoteReceiver;
			
			Logger.init(getActivity(), receivers);
			
			t.start();
						
			t2.start();			

			t3.start();		

			t4.start();
			
			t5.start();			
			
			t6.start();			
			
			//t7.start();			

		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			Logger.close();
		}
		
		
		
		
	}

}
