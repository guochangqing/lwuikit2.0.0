package six.lwuikit.saf;

public interface AEventInterface {
	//开启动画
	public void doStartAnimation(ActionEntity entity);
	//base记录自己注册的内容，以便回收使用
	public void doRecord(String eid,String aid);
	
	public int getActionIdentifier();
	/**
	 * 
	 * 计算坐标
	 * 
	 * @param type 类型
	 * 
	 * */
	public void doCalLocation(ActionEntity entity,String type);
	
	public void doDestroy();
	
	public void onAnimationPrepare(ActionEntity en);
	
	public void onAnimationStart(ActionEntity en);
	
	public void onAnimationEnd(ActionEntity en);
	
	public void onFinishLayout();
}
