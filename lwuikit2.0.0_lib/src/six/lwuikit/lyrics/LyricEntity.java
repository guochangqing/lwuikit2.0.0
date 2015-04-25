package six.lwuikit.lyrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

public class LyricEntity extends EntityBase{
	// artist
	private String ar;
	// songname
	private String ti;
	// album
	private String al;
	//vertion
	private String ver;
	// editor
	private String by;
	// Time offset value,The unit is milliseconds, a positive value indicates
	// that the overall advance, negative contrast. This is used to adjust the
	// display speed of the overall
	private String offset;
	
	private int offsetTime = 0;
	
	private List<RowEntity> allrows;
	
	private int mHignlightRow = -1;
	
	private float mPaddingY;
	
	public LyricEntity() {
		mHignlightRow = -1;
	}
	public void reset() {
		mHignlightRow = -1;
	}
	synchronized public int obtainHighlightRowByTime(long time) {
		if(null == allrows || allrows.size() <= 0) {
			mHignlightRow = -1;
			return mHignlightRow;
		}
		int i = 0;
		for(RowEntity temp : allrows) {
			if(temp.getTime() > time){
				break;
			}
			i ++ ;
		}
		mHignlightRow = i - 1;
		return mHignlightRow;
	}
	synchronized public int getHignlightRow() {
		return mHignlightRow;
	}
	synchronized public void setHighlightRow(int position) {
		mHignlightRow = position;
	}
	public float getPaddingY() {
		return mPaddingY;
	}
	public void setPaddingY(float mPaddingY) {
		this.mPaddingY = mPaddingY;
	}
	synchronized public void addRowEntity(RowEntity row) {
		if(null == row) {
			return;
		}
		if(null == allrows){
			allrows = new ArrayList<RowEntity>();
		}
		allrows.add(row);
	}
	synchronized public int sizeOfLyric() {
		return null == allrows ? 0 : allrows.size();
	}
	synchronized public RowEntity getRowEntity(int index) {
		if(null == allrows || index < 0 || index >= allrows.size()){
			return null;
		}
		return allrows.get(index);
	}
	synchronized public void sortRows() {
		if(null == allrows || allrows.size() <= 0){
			return;
		}
		Collections.sort(allrows);
	}
	synchronized public void destroy() {
		if(null != allrows) {
			allrows.clear();
			allrows = null;
		}
	}
	@Override
	public void onDraw(Canvas canvas, Paint hpaint, Paint mpaint,boolean highlight) {
		// TODO Auto-generated method stub
		if(null == allrows || allrows.size() <= 0){
			return;
		}
		boolean hl = false;
		for(int i = 0 ;i < allrows.size() ; i++) {
			if(i == mHignlightRow){
				hl = true;
			}else{
				hl = false;
			}
			allrows.get(i).onDraw(canvas, hpaint, mpaint, hl);
		}
	}
	@Override
	public Rect onLayout(Rect root,Rect last,Paint hpaint,Paint mpaint) {
		// TODO Auto-generated method stub
		setRootRect(root);
		if(null == allrows || allrows.size() <= 0){
			setSelfRect(last);
			return null;
		}
		Rect templast = new Rect();
		selfrect.left = root.left;
		selfrect.top = last.bottom;
		selfrect.right = root.right;
		selfrect.bottom = last.bottom;
		templast.set(last);
		for(RowEntity temp : allrows) {
			temp.setPaddingY(mPaddingY);
			templast.set(temp.onLayout(root, templast, hpaint, mpaint));
			selfrect.bottom = selfrect.bottom + templast.height();
		}
		return null;
	}
	@Override
	public Rect onScroll(Rect root,Rect last) {
		// TODO Auto-generated method stub
		if(null == allrows || allrows.size() <= 0){
			return null;
		}
		Rect templast = new Rect();
		selfrect.top = last.bottom;
		selfrect.bottom = last.bottom;
		templast.set(last);
		for(RowEntity temp : allrows) {
			templast.set(temp.onScroll(root, templast));
			selfrect.bottom = selfrect.bottom + templast.height();
		}
		return null;
	}
	synchronized public String obtainLyricStr() {
		StringBuffer buffer = new StringBuffer();
		if(null != ar){
			buffer.append("[ar:"+ar+"]\r\n");
		}
		if(null != ti){
			buffer.append("[ti:"+ti+"]\r\n");
		}
		if(null != al){
			buffer.append("[al:"+al+"]\r\n");
		}
		if(null != ver){
			buffer.append("[ver:"+ver+"]\r\n");
		}
		if(null != by){
			buffer.append("[by:"+by+"]\r\n");
		}
		if(null != offset){
			buffer.append("[offset:"+offset+"]\r\n");
		}
		for(RowEntity temp : allrows){
			String str = temp.obtainRowStr();
			if(null != str){
				buffer.append(str);
			}
		}
		return buffer.toString();
	}
	public String getArtist() {
		return ar;
	}
	public void setArtist(String ar) {
		this.ar = ar;
	}
	public String getTitle() {
		return ti;
	}
	public void setTitle(String ti) {
		this.ti = ti;
	}
	public String getAlbum() {
		return al;
	}
	public void setAlbum(String al) {
		this.al = al;
	}
	public String getVersion() {
		return ver;
	}
	public void setVersion(String ver) {
		this.ver = ver;
	}
	public String getByEditor() {
		return by;
	}
	public void setByEditor(String by) {
		this.by = by;
	}
	synchronized public String getOffset() {
		return offset;
	}
	synchronized public void setOffset(String offset) {
		if(!TextUtils.isEmpty(offset) && 
				isNumeric(offset)){
			this.offset = offset;
			offsetTime = Integer.parseInt(offset);
		}else{
			this.offset = "0";
			offsetTime = 0;
		}
	}
	private boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	} 
	synchronized public void setOffsetTime(int otime) {
		offsetTime = otime;
		this.offset = String.valueOf(otime);
	}
	synchronized public int getOffsetTime() {
		return this.offsetTime;
	}
}
