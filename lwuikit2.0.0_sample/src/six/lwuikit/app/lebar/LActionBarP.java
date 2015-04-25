package six.lwuikit.app.lebar;

import six.lwuikit.R;
import six.lwuikit.saf.AInfo;
import six.lwuikit.saf.ActionEntity;
import six.lwuikit.saf.LinearBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class LActionBarP extends LinearBase{
	
	public LActionBarP(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public void onAnimationPrepare(ActionEntity en) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onAnimationStart(ActionEntity en) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		findViewById(R.id.actionbar_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onAnimationEnd(ActionEntity en) {
		// TODO Auto-generated method stub
		String eventid = en.getString(AInfo.eventid);
		if("1".equals(eventid)){
			
			String actionid = en.getString(AInfo.actionid);
			if("9".equals(actionid)){
				findViewById(R.id.actionbar_play).setVisibility(View.VISIBLE);
			}
		}else if("2".equals(eventid)){
			String actionid = en.getString(AInfo.actionid);
			if("9".equals(actionid)){
				findViewById(R.id.actionbar_play).setVisibility(View.INVISIBLE);
			}
		}
	}
	
	@Override
	public void doCalLocation(ActionEntity entity, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishLayout() {
		// TODO Auto-generated method stub
		
	}

	
}
