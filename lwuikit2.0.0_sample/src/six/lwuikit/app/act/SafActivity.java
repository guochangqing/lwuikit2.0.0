package six.lwuikit.app.act;

import six.lwuikit.R;
import six.lwuikit.app.lebar.MsgWrapper;
import six.lwuikit.app.lebar.PlayBarPlaceHolder;
import six.lwuikit.saf.DataFetchInterface;
import six.lwuikit.saf.LinearBase;
import six.lwuikit.saf.OnLEventListener;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class SafActivity extends Activity implements OnClickListener{
	PlayBarPlaceHolder holders;
	boolean isPlayScreen = false;
	Button test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_saf);
		((LinearBase)findViewById(R.id.ceshi)).setDataFetchInterface(myFectch);
		test = (Button)findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(SafActivity.this);
				EditText edit = new EditText(SafActivity.this);
				edit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,200));
				dialog.setContentView(edit);
				dialog.show();
			}
		});
		holders = (PlayBarPlaceHolder)findViewById(R.id.holders);
		holders.setOnAttachListener(new PlayBarPlaceHolder.AttachListener() {
			
			@Override
			public void onAttachFinish(PlayBarPlaceHolder view) {
				// TODO Auto-generated method stub
				holders.findViewWithId(R.id.playbar_cover).setOnClickListener(SafActivity.this);
			}
			
			@Override
			public void onAttachError(PlayBarPlaceHolder view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isPlayScreen){
			MsgWrapper.dismissPlayContentInAnim(l);
		}else{
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.playbar_cover){
			MsgWrapper.showPlayContentInAnim(l);
		}
	}
	public OnLEventListener l = new OnLEventListener(){

		@Override
		public void onEventStart(String eventid) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onEventEnd(String eventid) {
			// TODO Auto-generated method stub
			if("1".equals(eventid)){
				isPlayScreen = true;
			}else if("2".equals(eventid)){
				isPlayScreen = false;
			}
		}

		@Override
		public void onEventAbandon(String eventid) {
			// TODO Auto-generated method stub
			Log.e("aa", "onEventAbandon:"+eventid);
		}
	};
	public DataFetchInterface myFectch = new DataFetchInterface() {
		
		@Override
		public int getViewIdByName(String viewname) {
			// TODO Auto-generated method stub
			if("component1".equals(viewname)){
				return R.id.component1;
			}else if("component2".equals(viewname)){
				return R.id.component2;
			}else if("expandsss".equals(viewname)){
				return R.id.expandsss;
			}else if("ceshi".equals(viewname)) {
				return R.id.ceshi;
			}
			return 0;
		}
		
		@Override
		public int getAnimationIdByName(String animname) {
			// TODO Auto-generated method stub
			if("playbar_fade_out".equals(animname)){
				return R.anim.playbar_fade_out;
			}else if("playbar_fade_in".equals(animname)){
				return R.anim.playbar_fade_in;
			}
			return 0;
		}
	};
}
