package six.lwuikit.app.lebar;

import six.lwuikit.R;
import six.lwuikit.saf.DataFetchInterface;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PlayBarPlaceHolder extends LinearLayout{

	private FrameLayout root;
	CollapseViewGroup group;
	AttachListener mAttachListener;
	
	
	public AttachListener getOnAttachListener() {
		return mAttachListener;
	}

	public void setOnAttachListener(AttachListener mAttachListener) {
		this.mAttachListener = mAttachListener;
	}

	private PlayBarPlaceHolder(Context context){
		super(context);
	}
	
	public PlayBarPlaceHolder(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	public View findViewWithId(int id){
		if(null != group){
			return group.findViewById(id);
		}
		return null;
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		PlayBar placeholder = (PlayBar)View.inflate(getContext(), R.layout.c_playbar, null);
//		placeholder.setRegSwitch(false);
		placeholder.setDataFetchInterface(myFectch);
		placeholder.setVisibility(View.INVISIBLE);
		this.addView(placeholder);
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		if(getContext() instanceof Activity){
			root = (FrameLayout)((Activity)getContext()).findViewById(R.id.playbar_father);
			group = (CollapseViewGroup)View.inflate(getContext(), R.layout.c_playgroup, null);
			group.setDataFetchInterface(myFectch);
			group.setDataFetchInterface(myFectch);
			((LActionBarP)group.findViewById(R.id.actionbar_play)).setDataFetchInterface(myFectch);
			((ContentPanel)group.findViewById(R.id.playcontent_container)).setDataFetchInterface(myFectch);
			((PlayBar)group.findViewById(R.id.playbar_container1)).setDataFetchInterface(myFectch);
			FrameLayout.LayoutParams paras = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			paras.gravity = Gravity.CENTER;
			group.setLayoutParams(paras);
			root.addView(group);
			if(null != mAttachListener){
				mAttachListener.onAttachFinish(this);
			}
		}else{
			if(null != mAttachListener){
				mAttachListener.onAttachError(this);
			}
		}
	}
	
	public DataFetchInterface myFectch = new DataFetchInterface() {
		
		@Override
		public int getViewIdByName(String viewname) {
			// TODO Auto-generated method stub
			if("bgdrawable_container".equals(viewname)){
				return R.id.bgdrawable_container;
			}else if("actionbar_play".equals(viewname)){
				return R.id.actionbar_play;
			}else if("playcontent_container".equals(viewname)){
				return R.id.playcontent_container;
			}else if("playcontent_container2".equals(viewname)) {
				return R.id.playcontent_container2;
			}else if("playbar_container5".equals(viewname)) {
				return R.id.playbar_container5;
			}else if("playbar_button4".equals(viewname)) {
				return R.id.playbar_button4;
			}else if("playbar_button1".equals(viewname)) {
				return R.id.playbar_button1;
			}else if("playbar_container4".equals(viewname)){
				return R.id.playbar_container4;
			}else if("bgdrawable_container".equals(viewname)){
				return R.id.bgdrawable_container;
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
			}else if("content_fade_in".equals(animname)){
				return R.anim.content_fade_in;
			}else if("content_fade_out".equals(animname)){
				return R.anim.content_fade_out;
			}
			return 0;
		}
	};
	
	public interface AttachListener{
		public void onAttachFinish(PlayBarPlaceHolder view);
		public void onAttachError(PlayBarPlaceHolder view);
	}
}
