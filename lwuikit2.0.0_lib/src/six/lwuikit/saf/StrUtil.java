package six.lwuikit.saf;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;


public class StrUtil {
	/**
	 * 拆分aEvent属性值,并生成实例
	 * */
	public static void splitToEntity(AEventInterface view,String str){
		if(null == str || 
				str.length() <= 0) {
			return;
		}
		String[] temp = str.split(";");
		if(null == temp || 
				temp.length <= 0) {
			return;
		}
		for(int i=0;i<temp.length;i++){
			String[] temp2 = temp[i].split(",");
			if(null == temp2 || 
					temp2.length <= 0) {
				continue;
			}
			ActionEntity entity = new ActionEntity();
			entity.putInt(AInfo.status,ActionEntity.Status_Idle);
			entity.putObject(AInfo.actionexcutor, view);
			entity.putString(AInfo.actionidentifier, view.getActionIdentifier()+"_");
			String[] ae = parseContent(temp2,entity);
			String actioncontent = ae[0];
			String eventid = ae[1];
			String actiontype = entity.getString(AInfo.actiontype);
			parseValue(entity,actiontype,actioncontent);
			if(null != eventid && eventid.length()>0){
				EventEntity evententity = null;
				if(AEventLooper.getLooper().contains(eventid)){
					evententity = AEventLooper.getLooper().getEventEntity(eventid);
				}else{
					evententity = new EventEntity(eventid);
					AEventLooper.getLooper().addEventEntity(eventid, evententity);
				}
				view.doRecord(eventid,entity.getString(AInfo.actionidentifier)+entity.getString(AInfo.actionid));
				entity.setActionListener(evententity);
				entity.putString(AInfo.eventid, eventid);
				evententity.addActionEntity(entity.getString(AInfo.actionidentifier)+entity.getString(AInfo.actionid),entity);
			}
		}
	}
	private static String[] parseContent(String[] temp2 , ActionEntity entity) {
		String[] ae = new String[2];
		for(int i = 0;i<2;i++){
			ae[i] = null;
		}
		for(int j=0;j<temp2.length;j++){
			String[] temp3 = temp2[j].split("=");
			if(null == temp3 || 
					temp3.length <= 0) {
				continue;
			}
			if(AInfo.actioncontent.equals(temp3[0])){
				ae[0] = temp3[1];
			}else if(AInfo.eventid.equals(temp3[0])){
				ae[1] = temp3[1];
			}else{
				entity.putString(temp3[0],temp3[1]);
			}
		}
		return ae;
	}
	private static void parseValue(ActionEntity entity,String actiontype,String actioncontent) {
		if(null == actiontype){
			return;
		}
		if("1".equals(actiontype)){
			if(null != actioncontent){
				entity.putString(AInfo.actionxml, actioncontent);
			}
		}else if("2".equals(actiontype)){
			if(null == actioncontent) {
				return;
			}
			String[] temp4 = actioncontent.split("\\|");
			if(null == temp4 || 
				temp4.length <= 0) {
				return;
			}
			if(temp4.length>0){
				entity.putString(AInfo.key_fx, temp4[0]);
			}
			if(temp4.length>1){
				entity.putString(AInfo.key_fy, temp4[1]);
			}
			if(temp4.length>2){
				entity.putString(AInfo.key_dx, temp4[2]);
			}
			if(temp4.length>3){
				entity.putString(AInfo.key_dy, temp4[3]);
			}
			if(temp4.length>4){
				entity.putString(AInfo.actionduration, temp4[4]);
			}
			if(temp4.length>5){
				entity.putString(AInfo.actioninterpolator, temp4[5]);
			}
		}
	}
	/**
	 * 获取变换器
	 * */
	public static Interpolator getInterpolator(String interpolator){
		Interpolator in = null;
		if( null == interpolator) {
			in = new Interpolator() {
				public float getInterpolation(float t) {
					t -= 1.0f;
					return t * t * t * t * t + 1.0f;
				}
			};
			return in;
		}
		if("AccelerateDecelerateInterpolator".equals(interpolator)){
			//在动画开始与结束的地方速率改变比较慢，在中间的时候加速
			in = new AccelerateDecelerateInterpolator();
		}else if("AccelerateInterpolator".equals(interpolator)){
			//在动画开始的地方速率改变比较慢，然后开始加速
			in = new AccelerateInterpolator();
		}else if("AnticipateInterpolator".equals(interpolator)){
			//开始的时候向后然后向前甩
			in = new AnticipateInterpolator();
		}else if("AnticipateOvershootInterpolator".equals(interpolator)){
			//开始的时候向后然后向前甩一定值后返回最后的值
			in = new AnticipateOvershootInterpolator();
		}else if("BounceInterpolator".equals(interpolator)){
			//动画结束的时候弹起
			in = new BounceInterpolator();
		}else if("CycleInterpolator".equals(interpolator)){
			//动画循环播放特定的次数，速率改变沿着正弦曲线
			in = new CycleInterpolator(1);
		}else if("DecelerateInterpolator".equals(interpolator)){
			//在动画开始的地方快然后慢
			in = new DecelerateInterpolator();
		}else if("OvershootInterpolator".equals(interpolator)){
			//向前甩一定值后再回到原来位置
			in = new OvershootInterpolator();
		}else if("LinearInterpolator".equals(interpolator)){
			//以常量速率改变
			in = new LinearInterpolator();
		}else{
			//自定义
			in = new Interpolator() {
				public float getInterpolation(float t) {
					t -= 1.0f;
					return t * t * t * t * t + 1.0f;
				}
			};
		}
		return in;
	}
	/**
	 * 定位初始与结束的位置
	 * 
	 * */
	public static void locationInView(View view,ActionEntity entity){
		if(null == view || null == entity){
			return;
		}
		locations(view,entity,AInfo.key_fx,AInfo.value_fx);
		locations(view,entity,AInfo.key_fy,AInfo.value_fy);
		locations(view,entity,AInfo.key_dx,AInfo.value_dx);
		locations(view,entity,AInfo.key_dy,AInfo.value_dy);
	}
	private static void locations(View view,ActionEntity entity,String key1,String key2){
		String fx = entity.getString(key1);
		if(null != fx){
			if("left".equals(fx)){
				entity.putString(key2, ""+view.getLeft());
			}else if("-left".equals(fx)){
				entity.putString(key2, "-"+view.getLeft());
			}else if("right".equals(fx)){
				entity.putString(key2, ""+view.getRight());
			}else if("-right".equals(fx)){
				entity.putString(key2, "-"+view.getRight());
			}else if("top".equals(fx)){
				entity.putString(key2, ""+view.getTop());
			}else if("-top".equals(fx)){
				entity.putString(key2, "-"+view.getTop());
			}else if("bottom".equals(fx)){
				entity.putString(key2, ""+view.getBottom());
			}else if("-bottom".equals(fx)){
				entity.putString(key2, "-"+view.getBottom());
			}else if("width".equals(fx)){
				entity.putString(key2, ""+view.getWidth());
			}else if("-width".equals(fx)){
				entity.putString(key2, "-"+view.getWidth());
			}else if("height".equals(fx)){
				entity.putString(key2, ""+view.getHeight());
			}else if("-height".equals(fx)){
				entity.putString(key2, "-"+view.getHeight());
			}else if(fx.endsWith("%w")){
				if(fx.startsWith("-")){
					int temp = Integer.parseInt(fx.substring(1,fx.length()-2));
					entity.putString(key2,"-"+ (view.getWidth()*temp/100));
				}else{
					int temp = Integer.parseInt(fx.substring(0,fx.length()-2));
					entity.putString(key2,""+ (view.getWidth()*temp/100));
				}
			}else if(fx.endsWith("%h")){
				if(fx.startsWith("-")){
					int temp = Integer.parseInt(fx.substring(1,fx.length()-2));
					entity.putString(key2,"-"+ (view.getHeight()*temp/100));
				}else{
					int temp = Integer.parseInt(fx.substring(0,fx.length()-2));
					entity.putString(key2,""+ (view.getHeight()*temp/100));
				}
			}else if("expression".equals(fx)){
				if(view instanceof AEventInterface){
					((AEventInterface) view).doCalLocation(entity, key1);
				}
			}else{
				//数值类型，直接插入
				entity.putString(key2,fx);
			}
		}
	}
}
