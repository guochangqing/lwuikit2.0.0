package six.lwuikit.saf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AEventLooper {
	
	private static AEventLooper mAEventLooper = new AEventLooper();
	private HashMap<String,EventEntity> maps;
	
	public static AEventLooper getLooper() {
		return mAEventLooper;
	}
	private AEventLooper(){
		maps = new HashMap<String,EventEntity>();
	}
	public boolean contains(String key){
		return maps.containsKey(key);
	}
	public int looperSize(){
		if(null == maps){
			return 0;
		}
		return maps.size();
	}
	public EventEntity getEventEntity(String key){
		return maps.get(key);
	}
	public void addEventEntity(String key,EventEntity value){
		maps.put(key, value);
	}
	public ActionEntity getActionEntity(String eventid,String actionid){
		if(null == maps || 
				!maps.containsKey(eventid)) {
			return null;
		}
		EventEntity entity = maps.get(eventid);
		if(null != entity){
			return entity.getActionEntity(actionid);
		}
		return null;
	}
	public void removeEntity(String eventid,String actionid){
		if(null == maps || 
				!maps.containsKey(eventid)) {
			return;
		}
		EventEntity entity = maps.get(eventid);
		if(null != entity){
			entity.removeActionEntity(actionid);
			if(entity.getSize()<=0){
				maps.remove(eventid);
			}
		}
	}
	public void destroy(){
		if(null == maps){
			return;
		}
		Iterator<Entry<String, EventEntity>> iter = maps.entrySet().iterator();
		ArrayList<EventEntity> l = new ArrayList<EventEntity>();
		while (iter.hasNext()) {
			Map.Entry<String, EventEntity> entry = (Map.Entry<String, EventEntity>) iter
					.next();
			EventEntity val = entry.getValue();
			if (null != val) {
				l.add(val);
			}
		}
		if (l.size() > 0) {
			for (EventEntity temp : l) {
				temp.destroy();
			}
		}
		l.clear();
		maps.clear();
		maps = null;
	}
	public void traverse(){
		Iterator<Entry<String, EventEntity>> iter = maps.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, EventEntity> entry = (Map.Entry<String, EventEntity>) iter.next();
			EventEntity val = entry.getValue();
			if(null != val){
				val.traverse();
			}
		}
	}
}
