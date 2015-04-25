package six.lwuikit.app.lebar;

import six.lwuikit.R;
import six.lwuikit.saf.AInfo;
import six.lwuikit.saf.ActionEntity;
import six.lwuikit.saf.FrameBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ContentPanel extends FrameBase{
	
	public ContentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onAnimationStart(ActionEntity en) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(ActionEntity en) {
		// TODO Auto-generated method stub
		String eventid = en.getString(AInfo.eventid);
		if("1".equals(eventid)){
			String actionid = en.getString(AInfo.actionid);
			if("12".equals(actionid)){
				findViewById(R.id.playcontent_container).setVisibility(View.VISIBLE);
			}
		}else if("2".equals(eventid)){
			String actionid = en.getString(AInfo.actionid);
			if("12".equals(actionid)){
				findViewById(R.id.playcontent_container).setVisibility(View.INVISIBLE);
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
