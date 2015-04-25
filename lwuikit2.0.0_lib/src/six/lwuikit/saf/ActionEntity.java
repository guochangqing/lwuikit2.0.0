package six.lwuikit.saf;

import java.util.HashMap;

import android.util.Log;


public class ActionEntity{
	private HashMap<String,Object> maps;
	
	public HashMap<String, Object> getMaps() {
		return maps;
	}
	//空闲状态
	public static final int Status_Idle = 0;
	//运行状态
	public static final int Status_Running = 2;
	//准备状态
	public static final int Status_prepare = 1;
	//等待
	public static final int Status_wait = 3;
	//监听器
	private OnLActionListener mOnLActionListener;
	
	
	public void setActionListener(OnLActionListener en){
		mOnLActionListener = en;
	}
	public ActionEntity(){
		maps = new HashMap<String,Object>();
	}
	public void sendStartToParent(){
		if(null != mOnLActionListener){
			mOnLActionListener.onActionStart(this);
		}
	}
	public void sendEndToParent(){
		if(null != mOnLActionListener){
			mOnLActionListener.onActionEnd(this);
		}
	}
	public void putObject(String key,Object value){
		if(null != key && null != value){
			maps.put(key, value);
		}
	}
	public void putInt(String key,int value){
		maps.put(key, value);
	}
	public void putString(String key,String value){
		if(null != key && null != value && value.length()>0){
			maps.put(key, value);
		}
	}
	public Object getObject(String key){
		return maps.get(key);
	}
	
	public int getInt(String key){
		if(null == maps) {
			return -1;
		}
		Object obj = maps.get(key);
		if(null == obj) {
			return -1;
		}
		if(obj instanceof Integer){
			return ((Integer) obj).intValue();
		}else if(obj instanceof String){
			return Integer.parseInt((String)obj);
		}
		return -1;
	}
	public String getString(String key){
		if(null == maps) {
			return null;
		}
		Object obj = maps.get(key);
		if(null == obj) {
			return null;
		}
		if(obj instanceof Integer){
			return String.valueOf(obj);
		}else if(obj instanceof String){
			return (String)obj;
		}
		return null;
	}
	public void clearA(){
		if(null != maps){
			maps.clear();
		}
		mOnLActionListener = null;
	}
	public void destroy(){
		mOnLActionListener = null;
		if(null == maps){
			return;
		}
		Object objtemp = getObject(AInfo.actionexcutor);
		if (null != objtemp && objtemp instanceof AEventInterface) {
			((AEventInterface) objtemp).doDestroy();
		}
		maps.clear();
		maps = null;
	}
	public void traverse(){
		Log.v("aa", "actionid:"+getString(AInfo.actionid));
		Log.v("aa", "actionidentifier:"+getString(AInfo.actionidentifier));
//		Log.v("aa", "actiontype:"+getInt(AInfo.actiontype));
//		Log.v("aa", "actiondelay:"+getInt(AInfo.actiondelay));
//		Log.v("aa", "fx:"+getString(AInfo.fx));
//		Log.v("aa", "fy:"+getString(AInfo.fy));
//		Log.v("aa", "tx:"+getString(AInfo.dx));
//		Log.v("aa", "ty:"+getString(AInfo.dy));
	}
}
