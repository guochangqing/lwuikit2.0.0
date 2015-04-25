package six.lwuikit.app.act;

import six.lwuikit.R;
import six.lwuikit.bar.IProgressBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BarActivity extends Activity implements OnClickListener{
	IProgressBar color_bar;
	IProgressBar color_bar2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_bar);
		color_bar = (IProgressBar)findViewById(R.id.color_progressBar);
		color_bar2 = (IProgressBar)findViewById(R.id.color_progressBar2);
		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				color_bar.startAnimation();
			}
		});
		findViewById(R.id.btn_end).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				color_bar.stopAnimation();
			}
		});
		findViewById(R.id.btn_show).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				color_bar.setVisibility(View.VISIBLE);
			}
		});
		findViewById(R.id.btn_hide).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				color_bar.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
