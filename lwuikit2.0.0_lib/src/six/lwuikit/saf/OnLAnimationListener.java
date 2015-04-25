package six.lwuikit.saf;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class OnLAnimationListener implements AnimationListener{

	ActionEntity mActionEntity;
	
	OnLActionListener callback;
	
	public OnLAnimationListener(ActionEntity entity,OnLActionListener listener){
		mActionEntity = entity;
		callback = listener;
	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		if(null != callback){
			callback.onActionStart(mActionEntity);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		try {
			if(null != callback){
				callback.onActionEnd(mActionEntity);
			}
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
