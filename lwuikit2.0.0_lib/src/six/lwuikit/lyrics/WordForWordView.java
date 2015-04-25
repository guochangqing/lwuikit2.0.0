package six.lwuikit.lyrics;


import java.util.ArrayList;
import java.util.List;

import six.lwuikit.saf.RE;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("DrawAllocation")
public class WordForWordView extends TextView{
	private static final float DEFAULT_FONTSIZE = 30.0f;
	private Paint cPaint;
	private Paint ncPaint;
	private Typeface mTexttypeface = Typeface.DEFAULT;
	private static float radius = 0;
	private static float dx = 2;
	private static float dy = -1;
	private LyricEntity entity;
	private String defaultstr = "";
	private List<LineDrawable> drawables;
	private int numrows = 2;
	private String directions;
	private List<Integer> directionlist;
	public WordForWordView(Context context) {
		this(context,null,0);
	}
	public WordForWordView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public WordForWordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, RE.styleable_lyric);
		if(null != a){
			int index = RE.get(RE.ATTR_DEFAULTSTRING);
			if(index >= 0){
				setDefaultstr(a.getString(index));
			}
			index = RE.get(RE.ATTR_HIGHLIGHTCOLOR);
			if(index >= 0){
				setHighLightColor(a.getColor(index, Color.GREEN));
			}
			index = RE.get(RE.ATTR_NORMALCOLOR);
			if(index >= 0){
				setNormalColor(a.getColor(index, Color.WHITE));
			}
			index = RE.get(RE.ATTR_FONTSIZE);
			if(index >= 0){
				setFontSize((int)a.getDimension(index, 30));
			}
			index = RE.get(RE.ATTR_NUMROWS);
			if(index >= 0){
				setNumRows(a.getInt(index, 2));
			}
			index = RE.get(RE.ATTR_DIRECTIONS);
			if(index >= 0){
				setDirections(a.getString(index));
			}
			a.recycle();
		}
	}
	
	private void init() {
		directionlist = new ArrayList<Integer>();
		drawables = new ArrayList<LineDrawable>();
		ncPaint = new Paint();
		ncPaint.setAntiAlias(true);
		ncPaint.setTypeface(mTexttypeface);
		ncPaint.setShadowLayer(radius, dx, dy, Color.BLACK);
		ncPaint.setTextSize(DEFAULT_FONTSIZE);
		ncPaint.setColor(Color.WHITE);
		ncPaint.setTextAlign(Paint.Align.LEFT);
		cPaint = new Paint();
		cPaint.setAntiAlias(true);
		cPaint.setTypeface(mTexttypeface);
		cPaint.setShadowLayer(radius, dx, dy, Color.BLACK);
		cPaint.setTextSize(DEFAULT_FONTSIZE);
		cPaint.setColor(Color.GREEN);
		cPaint.setTextAlign(Paint.Align.LEFT);
	}
	
	synchronized public  int selectIndex(long curtime,long duration){
		if(null == entity){
			return -1;
		}
		int index = entity.obtainHighlightRowByTime(curtime);
		int tempindex = index;
		int j = 0;
		for(LineDrawable temp : drawables){
			if(temp.getPosition() == index){
				break;
			}
			j ++;
		}
		if(index < 0){
			index =0;
		}
		for(int i= j;i<drawables.size();i++){
			setTemp(drawables.get(i),curtime,duration,index,tempindex);
			index++;
		}
		for(int i =0;i<j;i++){
			setTemp(drawables.get(i),curtime,duration,index,tempindex);
			index++;
		}
		return tempindex;
	}
	
	private void setTemp(LineDrawable temp,long curtime,long duration,int index,int cur) {
		if(temp.getPosition() == index){
			temp.update_cTime(curtime);
			if(cur == index){
				temp.setFocusable(true);
			}else{
				temp.setFocusable(false);
			}
			return;
		}
		temp.setFocusable(false);
		if(index < 0 || index >= entity.sizeOfLyric()) {
			temp.update_cTime(curtime);
			temp.update_x();
			return;
		}
		temp.setPosition(index);
		temp.setContent(getRowEntity(index).getContent());
		temp.set_secTime(getRowEntity(index).getTime(),
				(index + 1 >= entity.sizeOfLyric() ? duration
						: getRowEntity(index + 1).getTime()), curtime);
	}
	public void setFontSize(int mFontSize) {
		cPaint.setTextSize(mFontSize);
		ncPaint.setTextSize(mFontSize);
		for(LineDrawable temp : drawables){
			temp.update_len();
			if(temp.getX() >= 0){
				temp.update_x();
			}
			temp.update_y();
			temp.update_cTime(temp.get_cTime());
		}
		postInvalidate();
	}
	
	public int getFontSize() {
		return (int)cPaint.getTextSize();
	}
	
	public void setLyric(LyricEntity en) {
		for(LineDrawable temp : drawables){
			temp.setPosition(-2);
		}
		if(null != entity){
			entity.reset();
		}
		entity = en;
		if(null != entity) {
			entity.reset();
		}
	}
	
	public void setHighLightColor(int color) {
		cPaint.setColor(color);
		postInvalidate();
	}

	public int getHighLightColor() {
		return cPaint.getColor();
	}
	
	public void setNormalColor(int color) {
		ncPaint.setColor(color);
		postInvalidate();
	}
	
	public int getNormalColor() {
		return ncPaint.getColor();
	}
	
	public int getNumRows() {
		return numrows;
	}
	public void setNumRows(int numrows) {
		this.numrows = numrows;
	}
	
	
	public String getDirections() {
		return directions;
	}
	public void setDirections(String directions) {
		this.directions = directions;
	}
	private RowEntity getRowEntity(int index) {
		if (null == entity || index < 0 || index >= entity.sizeOfLyric()) {
			return null;
		}
		return entity.getRowEntity(index);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(null == entity){
			cPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(defaultstr, getWidth()/2, getHeight()/2, cPaint);
			return;
		}
		cPaint.setTextAlign(Paint.Align.LEFT);
		for(LineDrawable temp : drawables){
			temp.draw(canvas);
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(drawables.size() != numrows){
			drawables.clear();
			directionlist.clear();
			if(!TextUtils.isEmpty(directions)){
				String[] sts = directions.split(",");
				if(null != sts && sts.length > 0){
					for(int i=0;i<sts.length;i++){
						directionlist.add(Integer.parseInt(sts[i]));
					}
				}
			}
			int h = getHeight()/numrows;
			for(int i=0;i<numrows;i++){
				drawables.add(new LineDrawable(cPaint, ncPaint, new Rect(0,h*i,getWidth(),h*i+h),getD(i)));
			}
		}else{
			int h = getHeight()/numrows;
			for(int i=0;i<numrows;i++){
				drawables.get(i).setSelfRect(new Rect(0,h*i,getWidth(),h*i+h));
			}
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		
	}
	private boolean getD(int index) {
		if(index < 0 ||index >= directionlist.size()){
			return true;
		}
		return directionlist.get(index)==1?true:false;
	}
	public String getDefaultstr() {
		return defaultstr;
	}
	public void setDefaultstr(String defaultstr) {
		this.defaultstr = defaultstr;
	}
	
	class LineDrawable {
		private String content = "......";
		private int position = -2;
		private boolean focusable = false;
		private long sTime;
		private long eTime;
		private long cTime;
		private Rect selfrect;
		private Paint hPaint;
		private Paint mPaint;
		private float len = 0;
		private float percent = 0.0f;
		private boolean direction = true;
		private float x = 0.0f;
		private float y = 0.0f;
		public Paint gethPaint() {
			return hPaint;
		}
		public Paint getmPaint() {
			return mPaint;
		}
		public String getContent() {
			return content;
		}
		
		public float getX() {
			return x;
		}
		public float getY() {
			return y;
		}
		public void setContent(String c) {
			if(!TextUtils.isEmpty(c)){
				content = c;
			}else{
				content = "......";
			}
			dis = 0.0f;
			update_len();
			update_x();
			update_y();
		}
		public void update_len() {
			len = hPaint.measureText(content);
		}
		public void update_x() {
			if(direction) {
				x = selfrect.left;
			} else {
				if(len >= selfrect.width()) {
					x = selfrect.left;
				}else{
					x = selfrect.left + selfrect.width()-len;
				}
			}
		}
		public void update_y() {
			FontMetricsInt fontMetrics = hPaint.getFontMetricsInt();
			y = selfrect.top + (selfrect.bottom - selfrect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top; 
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public void setFocusable(boolean focusable) {
			this.focusable = focusable;
		}
		public void set_secTime(long s,long e,long c) {
			sTime = s;
			eTime = e;
			update_cTime(c);
		}
		private float dis = 0.0f;
		public void update_cTime(long c) {
			cTime = c;
			percent = ((float)(cTime - sTime))/(eTime - sTime);
			if(percent < 0.0f) {
				percent = 0.0f;
			}
			if(percent > 1.0f) {
				percent = 1.0f;
			}
			if(focusable && len > selfrect.width()) {
				if(dis > 0){
					float v = dis / 10;
					if(dis - v > 2){
						dis -= v;
					}else{
						v = dis;
						dis = 0.0f;
					}
					x -= v;
					return;
				}
				float temp = percent * len;
				if(x + temp >= selfrect.right) {
					if(len - (selfrect.right-x) > selfrect.width()) {
						dis = selfrect.width();
					}else{
						dis = len - (selfrect.right-x);
					}
				}
			}
		}
		public long get_cTime() {
			return cTime;
		}
		public void setSelfRect(Rect selfrect) {
			this.selfrect = selfrect;
		}
		
		public boolean isDirection() {
			return direction;
		}
		LineDrawable(Paint h,Paint m,Rect r,boolean d) {
			selfrect = r;
			hPaint = h;
			mPaint = m;
			direction = d;
		}
		
		public void draw(Canvas canvas) {
			if(!focusable){
				canvas.drawText(content, x, y, mPaint);
				return;
			}
			hPaint.setShader(new LinearGradient(x, y, x+len, y, new int[] {hPaint.getColor(), mPaint.getColor()}, new float[] { percent, percent}, TileMode.CLAMP));
			canvas.drawText(content, x, y, hPaint);
			hPaint.setShader(null);
		}
	}
}
