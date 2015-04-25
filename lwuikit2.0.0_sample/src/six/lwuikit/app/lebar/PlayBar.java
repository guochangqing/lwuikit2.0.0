package six.lwuikit.app.lebar;

import six.lwuikit.R;
import six.lwuikit.saf.AInfo;
import six.lwuikit.saf.ActionEntity;
import six.lwuikit.saf.FrameBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class PlayBar extends FrameBase{
	View playbar_container4;
	View playbar_button1;
	View playbar_container2;
	View playbar_container3;
	View playbar_button4;
	public PlayBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onAnimationStart(ActionEntity en) {
		// TODO Auto-generated method stub
	
	}
	@Override
	public void onFinishLayout() {
		// TODO Auto-generated method stub
		playbar_container4.scrollTo(-playbar_button1.getWidth(), 0);
		ViewGroup.LayoutParams p = playbar_container3.getLayoutParams();
		p.width = playbar_container2.getWidth();
		playbar_container2.setLayoutParams(p);
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		playbar_container4 = findViewById(R.id.playbar_container4);
		playbar_button1 = findViewById(R.id.playbar_button1);
		playbar_container2 = findViewById(R.id.playbar_container2);
		playbar_container3 = findViewById(R.id.playbar_container3);
		playbar_button4 = findViewById(R.id.playbar_button4);
	}
	@Override
	public void onAnimationEnd(ActionEntity en) {
		// TODO Auto-generated method stub
		String eventid = en.getString(AInfo.eventid);
		if("1".equals(eventid)){
			String actionid = en.getString(AInfo.actionid);
			if("2".equals(actionid)){
				findViewById(R.id.playbar_container5).setVisibility(View.INVISIBLE);
			}else if("3".equals(actionid)){
				findViewById(R.id.playbar_button4).setVisibility(View.VISIBLE);
			}
		}else if("2".equals(eventid)){
			String actionid = en.getString(AInfo.actionid);
			if("2".equals(actionid)){
				findViewById(R.id.playbar_button4).setVisibility(View.INVISIBLE);
			}else if("3".equals(actionid)){
				findViewById(R.id.playbar_container5).setVisibility(View.VISIBLE);
			}
		}
		
	}

	@Override
	public void doCalLocation(ActionEntity entity, String type) {
		// TODO Auto-generated method stub
		String eventid = entity.getString(AInfo.eventid);
		if("1".equals(eventid)){
			String actionid = entity.getString(AInfo.actionid);
			if("11".equals(actionid)){
				if(AInfo.key_fx.equals(type)){
					entity.putInt(AInfo.value_fx,-playbar_button1.getWidth());
				}else if(AInfo.key_dx.equals(type)){
					entity.putInt(
							AInfo.value_dx,
							playbar_button1.getWidth()
									+ (this.getWidth()
											- playbar_button1.getWidth()
											- playbar_button4.getWidth() - playbar_container2
												.getWidth()) / 2);
				}
			}
		}else if("2".equals(eventid)){
			String actionid = entity.getString(AInfo.actionid);
			if("11".equals(actionid)){
				if(AInfo.key_fx.equals(type)){
					entity.putInt(AInfo.value_fx,(this.getWidth()-playbar_button4.getWidth()-playbar_container2.getWidth()-playbar_button1.getWidth())/2);
				}else if(AInfo.key_dx.equals(type)){
					entity.putInt(AInfo.value_dx,-(this.getWidth()-playbar_button4.getWidth()-playbar_container2.getWidth()-playbar_button1.getWidth())/2-playbar_button1.getWidth());
				}
			}
		}
	}
}
