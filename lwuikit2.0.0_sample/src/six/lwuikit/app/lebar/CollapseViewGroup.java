package six.lwuikit.app.lebar;

import six.lwuikit.R;
import six.lwuikit.saf.AInfo;
import six.lwuikit.saf.ActionEntity;
import six.lwuikit.saf.FrameBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;


public class CollapseViewGroup extends FrameBase implements OnClickListener{
	View bg_container;
	View playbar_container;
	public CollapseViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onFinishLayout() {
		// TODO Auto-generated method stub
		bg_container.scrollTo(0, -this.getBottom()+playbar_container.getHeight());
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		bg_container = findViewById(R.id.bgdrawable_container);
		playbar_container = findViewById(R.id.playbar_container1);
		findViewById(R.id.bgdrawable_content).setOnClickListener(this);
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
	public void onAnimationEnd(ActionEntity en) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCalLocation(ActionEntity entity, String type) {
		// TODO Auto-generated method stub
		String eventid = entity.getString(AInfo.eventid);
		if("1".equals(eventid)){
			String actionid = entity.getString(AInfo.actionid);
			if("1".equals(actionid)){
				if(AInfo.key_fy.equals(type)){
					entity.putInt(AInfo.value_fy,playbar_container.getHeight()-this.getHeight());
				}else if(AInfo.key_dy.equals(type)){
					entity.putInt(AInfo.value_dy,this.getHeight()-playbar_container.getHeight());
				}
			}
		}else if("2".equals(eventid)){
			String actionid = entity.getString(AInfo.actionid);
			if("1".equals(actionid)){
				if(AInfo.key_dy.equals(type)){
					entity.putInt(AInfo.value_dy,playbar_container.getHeight()-this.getHeight());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
