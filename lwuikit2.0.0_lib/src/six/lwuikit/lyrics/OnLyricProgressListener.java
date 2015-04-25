package six.lwuikit.lyrics;


public interface OnLyricProgressListener {
	public void onStartTracking(LineByLineView view) ;
	public void onHighLightChanged(LineByLineView view,long curtime,int highlightrow);
	public void onStopTracking(LineByLineView view) ;
}
