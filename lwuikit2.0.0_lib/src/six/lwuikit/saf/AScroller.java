package six.lwuikit.saf;

import android.content.Context;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class AScroller extends Scroller{
	
	private ActionEntity mActionEntity;//动作
	
	private View scrollview;//将要滚动的视图
	
	public AScroller(Context context, Interpolator interpolator,View view,ActionEntity entity) {
		super(context, interpolator);
		// TODO Auto-generated constructor stub
		scrollview = view;
		mActionEntity = entity;
	}
	public void destroy(){
		mActionEntity = null;
		scrollview = null;
	}
	public ActionEntity getActionEntity() {
		return mActionEntity;
	}

	public View getScrollview() {
		return scrollview;
	}
}
