package six.lwuikit.app.act;

import six.lwuikit.R;
import six.lwuikit.app.utils.LyricManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class WLActivity extends Activity implements OnClickListener{
	WakeLock mLrcWakeLock = null;
	String[][] info = new String[][]{{"a","1"},
									{"b","2"},
									{"d","4"},
									{"c","3"},
									{"d","4"},
									{"e","5"},
									{"d","4"},
									{"h","8"},
									{"f","6"},
									{"d","4"},
									{"g","7"},
									{"d","4"},
									{"h","8"}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wakelock);
		IntentFilter f = new IntentFilter();
		f.addAction(LyricManager.ACTION_LYRIC_CHANGED);
		this.registerReceiver(receiver, f);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiver);
		super.onBackPressed();
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Log.e("aa", "response:::"+arg1.getStringExtra(LyricManager.PARAMS_SONG_NAME));
		}
		
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.acquire_button) {
			acquireLrcWakeLock();
		}else if (id == R.id.release_button) {
			releaseLrcWakeLock();
		}else if(id == R.id.thread_button) {
			for(int i=0;i<info.length;i++) {
				Intent intent = new Intent();
				intent.setAction(LyricManager.ACTION_LOAD_LYRIC);
				intent.putExtra(LyricManager.PARAMS_SONG_NAME, info[i][0]);
				intent.putExtra(LyricManager.PARAMS_ARTIST_NAME, info[i][1]);
				this.sendBroadcast(intent);
			}
		}
	}
	@SuppressWarnings("deprecation")
	protected void acquireLrcWakeLock() {
		if(null == mLrcWakeLock){
			PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			mLrcWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MediaWakeLock"); 
		}
		Log.d("WLActivity", "WakeLock__isHeld:::"+mLrcWakeLock.isHeld());
		if (!mLrcWakeLock.isHeld()) {
			mLrcWakeLock.acquire(); 
			Log.d("WLActivity", "WakeLock__acquire");
		}
	}
	
	protected void releaseLrcWakeLock() {
		if( null != mLrcWakeLock) {
			Log.d("WLActivity", "WakeLock__isHeld:::"+mLrcWakeLock.isHeld());
			mLrcWakeLock.release();
			mLrcWakeLock = null;
			Log.d("WLActivity", "WakeLock__release");
		}
	}
}
