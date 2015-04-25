package six.lwuikit.saf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class EventEntity implements OnLActionListener{
	//事件id
	private volatile String eventid;
	//事件是否可执行
	private volatile boolean isRun;
	//动作实体
	private OnLEventListener callback;
	
	public OnLEventListener getCallback() {
		return callback;
	}
	public void setCallback(OnLEventListener callback) {
		this.callback = callback;
	}
	private HashMap<String,ActionEntity> maps;
	
	private volatile Handler handler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			synchronized (this) {
				if(null == msg || 
						msg.what != 2){
					return;
				}
				ActionEntity val = maps.get(""+msg.obj+msg.arg2);
				if(null == val) {
					return;
				}
				Object obj = val.getObject(AInfo.actionexcutor);
				if(null != obj && obj instanceof AEventInterface){
					((AEventInterface)obj).doStartAnimation(val);
				}
			}
		}
	};
	
	public HashMap<String,ActionEntity> getMap(){
		return maps;
	}
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public boolean isExcute(){
		return !isRun&&maps.size()>0;
	}
	
	public EventEntity(String id){
		isRun = false;
		eventid = id;
		maps = new HashMap<String,ActionEntity>();
	}
	public ActionEntity getActionEntity(String actionid){
		if(null == maps || 
				!maps.containsKey(actionid)) {
			return null;
		}
		return maps.get(actionid);
	}
	public int getSize(){
		return maps.size();
	}
	public void addActionEntity(String key,ActionEntity value){
		maps.put(key, value);
	}
	public void removeActionEntity(String key){
		ActionEntity entity = maps.get(key);
		if(null != entity){
			entity.clearA();
		}
		maps.remove(key);
	}
	public void doStartAnimation(OnLEventListener callback){
		synchronized(this){
			isRun = true;
			setCallback(callback);
			if(null != callback){
				callback.onEventStart(eventid);
			}
			List<ActionEntity> list = new ArrayList<ActionEntity>();
			Iterator<Entry<String, ActionEntity>> iter = maps.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ActionEntity> entry = (Map.Entry<String, ActionEntity>) iter
						.next();
				ActionEntity val2 = entry.getValue();
				val2.putInt(AInfo.status,ActionEntity.Status_wait);
				list.add(val2);
			}
			for(ActionEntity val:list){
				Object obj = val.getObject(AInfo.actionexcutor);
				if(null == obj) {
					continue;
				}
				int delay = val.getInt(AInfo.actiondelay);
				if (delay <= 0) {
					((AEventInterface) obj).doStartAnimation(val);
				} else {
					int actionid = val.getInt(AInfo.actionid);
					if(handler.hasMessages(2, val)){
						handler.removeMessages(2, val);
						val.putInt(AInfo.status,ActionEntity.Status_prepare);
						((AEventInterface) obj).onAnimationPrepare(val);
						((AEventInterface) obj).onAnimationStart(val);
						val.sendStartToParent();
						((AEventInterface) obj).onAnimationEnd(val);
						val.sendEndToParent();
					}
					if (actionid > -1) {
						Message msg = Message.obtain(handler, 2, val);
						msg.obj = val.getString(AInfo.actionidentifier);
						msg.arg2 = val.getInt(AInfo.actionid);
						handler.sendMessageDelayed(msg, delay);
					}
				}
			}
		}
	}
	int i = 0;
	int j = 0;
	int n = 0;
	@Override
	public void onActionStart(ActionEntity entity) {
		// TODO Auto-generated method stub
		if(null != entity){
			entity.putInt(AInfo.status,ActionEntity.Status_Running);
		}
	}
	@Override
	public void onActionEnd(ActionEntity entity) {
		synchronized(this){
		// TODO Auto-generated method stub
			if(null != entity){
				entity.putInt(AInfo.status,ActionEntity.Status_Idle);
			}
			int size = maps.size();
			int num = 0;
			Iterator<Entry<String, ActionEntity>> iter = maps.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ActionEntity> entry = (Map.Entry<String, ActionEntity>) iter.next();
				ActionEntity val = entry.getValue();
				if(null != val){
					if(val.getInt(AInfo.status) == ActionEntity.Status_Idle){
						num++;
					}
				}
			}
			if(num == size){
				isRun = false;
				if(null != callback){
					callback.onEventEnd(eventid);
				}
			}
		}
	}
	public void destroy(){
		if(null == maps){
			return;
		}
		Iterator<Entry<String, ActionEntity>> iter = maps.entrySet().iterator();
		ArrayList<ActionEntity> l = new ArrayList<ActionEntity>();
		while (iter.hasNext()) {
			Map.Entry<String, ActionEntity> entry = (Map.Entry<String, ActionEntity>) iter
					.next();
			ActionEntity val = entry.getValue();
			if (null != val) {
				l.add(val);
			}
		}
		if (l.size() > 0) {
			for (ActionEntity temp : l) {
				temp.destroy();
			}
		}
		l.clear();
		maps.clear();
		maps = null;
		handler = null;
	}
	public void traverse(){
		Iterator<Entry<String, ActionEntity>> iter = maps.entrySet().iterator();
		Log.e("aa", "size:"+maps.size());
		Log.e("aa", "eventid:"+eventid);
		Log.e("aa", "isRun:"+isRun);
		while (iter.hasNext()) {
			Map.Entry<String, ActionEntity> entry = (Map.Entry<String, ActionEntity>) iter.next();
			Log.e("aa", "key:"+entry.getKey());
			ActionEntity val = entry.getValue();
			if(null != val){
				val.traverse();
			}
		}
	}
}
