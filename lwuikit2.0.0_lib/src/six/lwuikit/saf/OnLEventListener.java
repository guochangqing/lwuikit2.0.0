package six.lwuikit.saf;

public interface OnLEventListener {

	public void onEventStart(String eventid);
	
	public void onEventEnd(String eventid);
	
	public void onEventAbandon(String eventid);
}
