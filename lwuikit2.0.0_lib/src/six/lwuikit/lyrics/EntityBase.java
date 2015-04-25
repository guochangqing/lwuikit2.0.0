package six.lwuikit.lyrics;

import android.graphics.Rect;

public abstract class EntityBase implements LyricLogicInterface{
	protected Rect rootrect;
	protected Rect selfrect;
	public EntityBase() {
		rootrect = new Rect();
		selfrect = new Rect();
	}
	
	public Rect getRootRect() {
		return rootrect;
	}
	public void setRootRect(Rect rootrect) {
		this.rootrect.set(rootrect);
	}
	public Rect getSelfRect() {
		return selfrect;
	}
	public void setSelfRect(Rect selfrect) {
		this.selfrect.set(selfrect);
	}
}
