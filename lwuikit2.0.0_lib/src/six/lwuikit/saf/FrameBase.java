/**
 * 
 * 每个FrameBase或者其派生对象，在同一时刻仅可包含一个actiontype=2的事件和多个actiontype=1的事件<br/>
 * 但是每个其子视图不可重复添加actiontype=2的事件，需要在同一个子视图添加多重动画，请选择使用AnimationSet
 * <font color="red">父容器如果是隐藏的，那么动画执行受影响</font>
 * @author guochangqing
 * 
 * */
package six.lwuikit.saf;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;


public class FrameBase extends FrameLayout implements AEventInterface{
	
	private AScroller mScroller;
	
	protected int identifier = -1;
	
	private volatile Handler mHandler;
	
	private HashMap<String, Integer> viewids = new HashMap<String,Integer>();
	
	private HashMap<String, Integer> animids = new HashMap<String,Integer>();
	
	private DataFetchInterface mDataFetchInterface;
	
	
	public DataFetchInterface getDataFetchInterface() {
		return mDataFetchInterface;
	}
	public void setDataFetchInterface(DataFetchInterface mDataFetchInterface) {
		this.mDataFetchInterface = mDataFetchInterface;
	}
	@Override
	public int getActionIdentifier() {
		// TODO Auto-generated method stub
		return identifier;
	}

	private String aEventStr = null;
	
	public String getEventStr() {
		return aEventStr;
	}
	public void setEventStr(String aEventStr) {
		this.aEventStr = aEventStr;
	}
	
	public AScroller getScroller() {
		return mScroller;
	}

	public FrameBase(Context context){
		this(context,null,0);
	}
	ArrayList<ABase> list;
	public FrameBase(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public FrameBase(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, RE.styleable_saf);
		if(null != a){
			int index = RE.get(RE.ATTR_AEVENT);
			if(index >= 0){
				setEventStr(a.getString(index));
			}
			a.recycle();
		}
	}
	
	private void init(){
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				synchronized (this) {
					if (msg.what == 1) {
						Bundle b = msg.getData();
						String eventid = b.getString(AInfo.eventid);
						String identifier = b.getString(AInfo.actionidentifier);
						String actionid = b.getString(AInfo.actionid);
						ActionEntity entity = AEventLooper
								.getLooper()
								.getActionEntity(eventid, identifier + actionid);
						if(null != entity){
							if(entity.getInt(AInfo.status) == ActionEntity.Status_prepare){
								FrameBase.this.onAnimationStart(entity);
								entity.sendStartToParent();
							}
							if(entity.getInt(AInfo.status) != ActionEntity.Status_Idle){
								FrameBase.this.onAnimationEnd(entity);
								entity.sendEndToParent();
							}
						}
					}else if(msg.what == 2){
						Bundle b = msg.getData();
						String eventid = b.getString(AInfo.eventid);
						String identifier = b.getString(AInfo.actionidentifier);
						String actionid = b.getString(AInfo.actionid);
						ActionEntity entity = AEventLooper.getLooper().getActionEntity(eventid, identifier+actionid);
						if(null != entity && entity.getInt(AInfo.status) == ActionEntity.Status_Running){
							FrameBase.this.onAnimationEnd(entity);
							entity.sendEndToParent();
						}
					}
				}
			}
		};
		list = new ArrayList<ABase>();
		identifier = AtomicCreator.getAndIncrement();
	}
	
	public boolean isFirstLayout() {
		return isFirstLayout;
	}
	public void setFirstLayout(boolean isFristLayout) {
		this.isFirstLayout = isFristLayout;
	}

	private boolean isFirstLayout = true;
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(isFirstLayout){
			isFirstLayout = false;
			onFinishLayout();
		}
	}
	@Override
	public void doStartAnimation(ActionEntity entity) {
		synchronized(this){
		// TODO Auto-generated method stub
			if(null != entity){
				String type = entity.getString(AInfo.actiontype);
				if("1".equals(type)){
					//关闭消息
					if(mHandler.hasMessages(1, entity)){
						mHandler.removeMessages(1, entity);
						if(entity.getInt(AInfo.status) == ActionEntity.Status_prepare){
							FrameBase.this.onAnimationStart(entity);
							entity.sendStartToParent();
						}
						if(entity.getInt(AInfo.status) != ActionEntity.Status_Idle){
							FrameBase.this.onAnimationEnd(entity);
							entity.sendEndToParent();
						}
					}
					String idname = entity.getString(AInfo.viewid);
					int id = -1;
					if(viewids.containsKey(idname)){
						id = viewids.get(idname);
					}else{
						if(null == mDataFetchInterface){
							return;
						}
						id = mDataFetchInterface.getViewIdByName(idname);
						viewids.put(idname, id);
					}
					View view = findViewById(id);
					//关闭动画
					if(null == view){
						//视图空，直接返回
						FrameBase.this.onAnimationPrepare(entity);
						FrameBase.this.onAnimationStart(entity);
						entity.sendStartToParent();
						FrameBase.this.onAnimationEnd(entity);
						entity.sendEndToParent();
						return;
					}
					view.clearAnimation();
					entity.putInt(AInfo.status, ActionEntity.Status_prepare);
					FrameBase.this.onAnimationPrepare(entity);
					String name = entity.getString(AInfo.actionxml);
					if(null == name){
						FrameBase.this.onAnimationStart(entity);
						entity.sendStartToParent();
						FrameBase.this.onAnimationEnd(entity);
						entity.sendEndToParent();
						return;
					}
					int animid = -1;
					if(animids.containsKey(name)){
						animid = animids.get(name);
					}else{
						if(null == mDataFetchInterface){
							return;
						}
						animid = mDataFetchInterface.getAnimationIdByName(name);
						animids.put(name, animid);
					}
					Animation animation = AnimationUtils.loadAnimation(getContext(),animid);
					animation.setAnimationListener(new OnLAnimationListener(entity,new OnLActionListener(){
						
						@Override
						public void onActionStart(ActionEntity en) {
							// TODO Auto-generated method stub
							if (null != en
									&& en.getInt(AInfo.status) == ActionEntity.Status_prepare) {
								FrameBase.this.onAnimationStart(en);
								en.sendStartToParent();
							}
						}

						@Override
						public void onActionEnd(ActionEntity en) {
							// TODO Auto-generated method stub
							if (null != en){
								if(en.getInt(AInfo.status) == ActionEntity.Status_prepare) {
									FrameBase.this.onAnimationStart(en);
									en.sendStartToParent();
								}
								if(en.getInt(AInfo.status) != ActionEntity.Status_Idle) {
									FrameBase.this.onAnimationEnd(en);
									en.sendEndToParent();
								}
							}
						}
					}));
					view.startAnimation(animation);
					sends(entity,1,(animation.getDuration()+animation.getStartOffset()));
				}else if("2".equals(type)){
					if(mHandler.hasMessages(2, entity)){
						mHandler.removeMessages(2, entity);
						if(entity.getInt(AInfo.status) == ActionEntity.Status_Running){
							FrameBase.this.onAnimationEnd(entity);
							entity.sendEndToParent();
						}
					}
					//移动动画
					if(null != mScroller){
						mScroller.abortAnimation();
						if(null != mScroller.getActionEntity()){
							if (mScroller.getActionEntity().getInt(AInfo.status) != ActionEntity.Status_Idle) {
								FrameBase.this.onAnimationEnd(mScroller.getActionEntity());
								mScroller.getActionEntity().sendEndToParent();
							}
						}
						mScroller.destroy();
						mScroller = null;
					}
					entity.putInt(AInfo.status, ActionEntity.Status_prepare);
					FrameBase.this.onAnimationPrepare(entity);
					FrameBase.this.onAnimationStart(entity);
					entity.sendStartToParent();
					StrUtil.locationInView(this, entity);
					if(entity.getInt(AInfo.value_dx)!=0||entity.getInt(AInfo.value_dy)!=0){
						String idname = entity.getString(AInfo.viewid);
						String interpolator = entity.getString(AInfo.actioninterpolator);
						int id = -1;
						if(viewids.containsKey(idname)){
							id = viewids.get(idname);
						}else{
							if(null == mDataFetchInterface){
								return;
							}
							id = mDataFetchInterface.getViewIdByName(idname);
							viewids.put(idname, id);
						}
						mScroller = new AScroller(getContext(),StrUtil.getInterpolator(interpolator),findViewById(id),entity);
						mScroller.startScroll(entity.getInt(AInfo.value_fx),
								entity.getInt(AInfo.value_fy), entity.getInt(AInfo.value_dx),
								entity.getInt(AInfo.value_dy),
								entity.getInt(AInfo.actionduration));
						sends(entity,2,entity.getInt(AInfo.actionduration));
					}else{
						FrameBase.this.onAnimationEnd(entity);
						entity.sendEndToParent();
					}
					invalidate();
				}
			}
		}
	}
	private void sends(ActionEntity entity,int what,long delay){
		Message msg = Message.obtain(mHandler, what, entity);
		Bundle b = new Bundle();
		b.putString(AInfo.eventid, entity.getString(AInfo.eventid));
		b.putString(AInfo.actionidentifier, entity.getString(AInfo.actionidentifier));
		b.putString(AInfo.actionid, entity.getString(AInfo.actionid));
		msg.setData(b);
		mHandler.sendMessageDelayed(msg, delay+30);
	}
	@Override
	public void doRecord(String eid, String aid) {
		// TODO Auto-generated method stub
		list.add(new ABase(eid,aid));
	}
	
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		isFirstLayout = true;
		regAllRecord();
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		isFirstLayout = true;
		removeAllRecord();
	}
	public void regAllRecord(){
		if(null != aEventStr){
			StrUtil.splitToEntity(this, aEventStr);
		}
	}
	//删除所有记录
	public void removeAllRecord(){
		if(null != list && list.size()>0){
			for(ABase b:list){
				AEventLooper.getLooper().removeEntity(b.eventid, b.actionid);
			}
			list.clear();
		}
	}
	@Override
	public final void computeScroll() {
		if(null != mScroller){
			if(mScroller.computeScrollOffset()){
				if(null != mScroller.getScrollview()){
					mScroller.getScrollview().scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
				}
				postInvalidate();
			}else{
				if(null != mScroller.getActionEntity()){
					if (mScroller.getActionEntity().getInt(AInfo.status) != ActionEntity.Status_Idle) {
						FrameBase.this.onAnimationEnd(mScroller.getActionEntity());
						mScroller.getActionEntity().sendEndToParent();
					}
				}
				mScroller.destroy();
				mScroller = null;
			}
		}
	}
	
	public void onAnimationPrepare(ActionEntity en){};
	
	public void onAnimationStart(ActionEntity en){};
	
	public void onAnimationEnd(ActionEntity en){};
	
	public void onFinishLayout(){};
	
	@Override
	public void doCalLocation(ActionEntity entity, String type) {}
	@Override
	public void doDestroy() {
		// TODO Auto-generated method stub
		if(null != viewids){
			viewids.clear();
			viewids = null;
		}
		if(null != animids){
			animids.clear();
			animids = null;
		}
		aEventStr = null;
		removeAllRecord();
		list = null;
		mHandler = null;
		mScroller = null;
	}
}
