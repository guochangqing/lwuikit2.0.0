package six.lwuikit.saf;


public class AEventHandler {
	AEventLooper looper;
	public AEventHandler(){
		looper = AEventLooper.getLooper();
	}
	/**
	 * 
	 * 等待上次事件完成后才能开始下一次
	 * 
	 * */
	public void sendMsgByEventid(String eventid,OnLEventListener handler){
		if(looper.contains(eventid)){
			EventEntity entity = looper.getEventEntity(eventid);
			if(entity.isExcute()){
				entity.doStartAnimation(handler);
				return;
			}
		}
		if(null != handler){
			handler.onEventAbandon(eventid);
		}
	}
	/**
	 * 
	 * 不需要等待上次事件完成就可以开始下一次（慎用）
	 * 
	 * */
	public void sendMsgByEventidNone(String eventid,OnLEventListener handler){
		if(looper.contains(eventid)){
			looper.getEventEntity(eventid).doStartAnimation(handler);
			return;
		}
		if(null != handler){
			handler.onEventAbandon(eventid);
		}
	}
}
