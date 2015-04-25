package six.lwuikit.app.lebar;

import six.lwuikit.saf.AEventHandler;
import six.lwuikit.saf.AEventLooper;
import six.lwuikit.saf.EventEntity;
import six.lwuikit.saf.OnLEventListener;

public class MsgWrapper {
	//所有事件id
	private static final String[] eventset = {"1","2"};
	
	/**
	 * 
	 * 显示播放界面（带动画）
	 * 
	 * */
	public static void showPlayContentInAnim(OnLEventListener callback){
		if(isShouldExcute()){
			new AEventHandler().sendMsgByEventid("1", callback);
		}
	}
	/**
	 * 
	 * 隐藏播放界面（带动画）
	 * 
	 * */
	public static void dismissPlayContentInAnim(OnLEventListener callback){
		if(isShouldExcute()){
			new AEventHandler().sendMsgByEventid("2", callback);
		}
	}
	public static boolean isShouldExcute(){
		AEventLooper looper = AEventLooper.getLooper();
		if(null == looper || looper.looperSize()<=0){
			return false;
		}
		for(int i=0;i<eventset.length;i++){
			EventEntity entity = looper.getEventEntity(eventset[i]);
			if(null != entity && !entity.isExcute()){
				return false;
			}
		}
		return true;
	}
}
