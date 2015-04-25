package six.lwuikit.lyrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import six.lwuikit.lyrics.XGestureDetector.OnXGestureListener;
import six.lwuikit.saf.RE;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * LineByLineView is View that render lyric and has a lot of API.
 * 
 * 
 * @author guochangqing
 * */
public class LineByLineView extends View{
	
	private int mHignlightRowColor = Color.WHITE;
	private int mNormalRowColor = Color.GREEN;
	private int mFontSize = 30;
	private float mPaddingY = 0.0f;
	private float mSrcOffsetY = -0.111111f;
	private float mOffsetY = mSrcOffsetY;
	private float mDefaultOffsetY = mOffsetY;
	private Paint mPaint;
	private Paint hPaint;
	private LyricEntity entity;
	private Rect viewrect;
	private Rect lastrect;
	private int scrollmode = 1;
	private int touchmode = 1;
	private boolean shadowable = false;
	private int shadowHeight = 0;
	private Paint shadowPaint;
	private int shadowFrontColor = Color.YELLOW;
	private int shadowEndColor = Color.GRAY;
	private LinearGradient shadowGradientUp;
	private LinearGradient shadowGradientDown;
	private OnLyricEventListener onLyricEventListener;
	private OnLyricProgressListener onLyricProgressListener;
	private int touchLineColor = Color.BLUE;
	private int touchLineHeight = 2;
	private boolean showTouchLine = false;
	private XGestureDetector mXGestureDetector;
	public LineByLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
		TypedArray a = context.obtainStyledAttributes(attrs, RE.styleable_lyric);
		if(null != a){
			int index = RE.get(RE.ATTR_OFFSET_Y);
			if(index >= 0){
				setOffsetY(a.getDimension(index, -0.111111f));
				mSrcOffsetY = mOffsetY;
			}
			index = RE.get(RE.ATTR_PADDING_Y);
			if(index >= 0){
				setPaddingY(a.getDimension(index, 0));
			}
			index = RE.get(RE.ATTR_HIGHLIGHTCOLOR);
			if(index >= 0){
				setHignlightRowColor(a.getColor(index, Color.GREEN));
			}
			index = RE.get(RE.ATTR_NORMALCOLOR);
			if(index >= 0){
				setNormalRowColor(a.getColor(index, Color.WHITE));
			}
			index = RE.get(RE.ATTR_FONTSIZE);
			if(index >= 0){
				setFontSize((int)a.getDimension(index, 30));
			}
			index = RE.get(RE.ATTR_TOUCHMODE);
			if(index >= 0){
				setTouchMode(a.getInt(index, 1));
			}
			index = RE.get(RE.ATTR_SCROLLMODE);
			if(index >= 0){
				setScrollMode(a.getInt(index, 1));
			}
			index = RE.get(RE.ATTR_SHADOWABLE);
			if(index >= 0){
				setShadowable(a.getBoolean(index, false));
			}
			index = RE.get(RE.ATTR_SHADOWHEIGHT);
			if(index >= 0){
				setShadowHeight((int)a.getDimension(index, 0));
			}
			index = RE.get(RE.ATTR_SHADOWFRONTCOLOR);
			if(index >= 0){
				setShadowFrontColor(a.getColor(index, Color.YELLOW));
			}
			index = RE.get(RE.ATTR_SHADOWENDCOLOR);
			if(index >= 0){
				setShadowEndColor(a.getColor(index, Color.GRAY));
			}
			index = RE.get(RE.ATTR_TOUCHLINEHEIGHT);
			if(index >= 0){
				setTouchLineHeight((int)a.getDimension(index, 2));
			}
			index = RE.get(RE.ATTR_TOUCHLINECOLOR);
			if(index >= 0){
				setTouchLineColor(a.getColor(index, Color.BLUE));
			}
			a.recycle();
		}
	}
	public LineByLineView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	public LineByLineView(Context context) {
		this(context,null);
	}
	private void init(Context context){
		viewrect = new Rect();
		lastrect = new Rect();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		hPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		hPaint.setColor(mHignlightRowColor);
		hPaint.setTextSize(mFontSize);
		hPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(mNormalRowColor);
		mPaint.setTextSize(mFontSize);
		mPaint.setTextAlign(Align.CENTER);
		mXGestureDetector = new XGestureDetector(context,mOnXGestureListener);
	}
	public void setLyric(LyricEntity en) {
		if(null == en) {
			return;
		}
		entity = en;
		entity.reset();
		entity.setPaddingY(mPaddingY);
		mOffsetY = mSrcOffsetY;
		measureAndLayout();
	}
	synchronized public void relayout() {
		if(null != entity){
			measureAndLayout();
		}
	}
	synchronized public void rescroll() {
		if(null != entity){
			lastrect.top = (int)mOffsetY;
			lastrect.bottom = (int)mOffsetY;
			entity.onScroll(viewrect, lastrect);
		}
	}
	synchronized public float speedLrc(){
		float speed=0;
		if(null != entity){
			RowEntity temp = entity.getRowEntity(entity.getHignlightRow());
			if(null != temp){
				if(temp.getSelfRect().top + temp.getSelfRect().height()/2 != mDefaultOffsetY){
					if(scrollmode == 0) {
						//line
						speed=((temp.getSelfRect().top + temp.getSelfRect().height()/2 - mDefaultOffsetY));
					}else if(scrollmode == 1) {
						//"pixel"
						speed=((temp.getSelfRect().top + temp.getSelfRect().height()/2 - mDefaultOffsetY)/10);
					}
					if (speed >= (mFontSize + mPaddingY)) {
						speed = mFontSize + mPaddingY;
					}
				}
			}
		}
		return speed;
	}
	
	synchronized public long getCurTime() {
		int id = getHightLightRow();
		if(id > -1) {
			return entity.getRowEntity(id).getTime();
		}
		return -1;
	}
	private int getHightLightRow() {
		if(null == entity){
			return -1;
		}
		for(int i=0;i<entity.sizeOfLyric();i++){
			Rect temp = entity.getRowEntity(i).getSelfRect();
			if (isIntersection(temp) || isGreater(temp)) {
				return i;
			}
		}
		return -1;
	}
	private boolean isIntersection(Rect temp) {
		if(temp.top <= mDefaultOffsetY && 
				temp.bottom >= mDefaultOffsetY) {
			return true;
		}
		return false;
	}
	private boolean isGreater(Rect temp) {
		if(temp.top > mDefaultOffsetY) {
			return true;
		}
		return false;
	}
	
	synchronized public  int selectIndex(long time){
		if(null == entity){
			return -1;
		}
		return entity.obtainHighlightRowByTime(time);
	}
	private volatile boolean isRun = false;	
	synchronized public boolean changeTime(String path,int ctime) {
		// TODO Auto-generated method stub
		if(null == entity){
			return false;
		}
		int time = entity.getOffsetTime();
		time += ctime;
		entity.setOffsetTime(time);
		isRun = false;
		runByDelay(path,time);
		return true;
	}
	private void runByDelay(String path,int time){
		if(null != myHandler){
			if(myHandler.hasMessages(MSG_SAVEFILE)){
				myHandler.removeMessages(MSG_SAVEFILE);
			}
			Message msg = Message.obtain(myHandler);
			msg.what = MSG_SAVEFILE;
			msg.obj = path;
			myHandler.sendMessageDelayed(msg, 100);
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(null != entity){
			entity.onDraw(canvas, hPaint, mPaint, false);
		}
		if (touchmode == 2 && showTouchLine) {
			int color = hPaint.getColor();
			hPaint.setColor(touchLineColor);
			canvas.drawRect(viewrect.left, viewrect.top
					+ (viewrect.height() - touchLineHeight) / 2,
					viewrect.right, viewrect.top
							+ (viewrect.height() + touchLineHeight) / 2, hPaint);
			hPaint.setColor(color);
		}
		if (shadowable) {
			if (null == shadowPaint) {
				shadowPaint = new Paint();
			}
			if (null == shadowGradientUp) {
				shadowGradientUp = new LinearGradient(viewrect.left,
						viewrect.top, viewrect.left, viewrect.top
								+ shadowHeight, new int[] { shadowFrontColor,
								shadowEndColor }, new float[] { 0.0f, 1.0f },
						TileMode.CLAMP);
			}
			shadowPaint.setShader(shadowGradientUp);
			canvas.drawRect(viewrect.left, viewrect.top, viewrect.right,
					viewrect.top + shadowHeight, shadowPaint);
			if (null == shadowGradientDown) {
				shadowGradientDown = new LinearGradient(viewrect.left,
						viewrect.bottom, viewrect.left, viewrect.bottom
								- shadowHeight, new int[] { shadowFrontColor,
								shadowEndColor }, new float[] { 0.0f, 1.0f },
						TileMode.CLAMP);
			}
			shadowPaint.setShader(shadowGradientDown);
			canvas.drawRect(viewrect.left, viewrect.bottom - shadowHeight,
					viewrect.right, viewrect.bottom, shadowPaint);
		}
	}
	private void measureAndLayout() {
		if(viewrect.left != 0){
			viewrect.left = 0;
			lastrect.left = 0;
		}
		if(viewrect.top != 0){
			viewrect.top = 0;
		}
		lastrect.top = (int)mOffsetY;
		if(viewrect.right != getWidth()){
			viewrect.right = getWidth();
			lastrect.right = getWidth();
		}
		if(viewrect.bottom != getHeight()){
			viewrect.bottom = getHeight();
		}
		lastrect.bottom = (int)mOffsetY;
		if(null != entity){
			entity.onLayout(viewrect,lastrect,hPaint, mPaint);
		}
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(shadowable){
			shadowGradientUp = null;
			shadowGradientDown = null;
		}
		if(mSrcOffsetY == -0.111111f){
			mSrcOffsetY = getHeight()/2;
			mOffsetY = mSrcOffsetY;
		}
		mDefaultOffsetY = getHeight()/2;
		measureAndLayout();
	}
	
	
	@Override
	public  boolean onTouchEvent(MotionEvent event) {
		if(touchmode == 0 || null == entity){
			return super.onTouchEvent(event);
		}
		return mXGestureDetector.onTouchEvent(event); 
	}
	
	private static final int MSG_SAVEFILE = 0x0001;
	
	private Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SAVEFILE:
				isRun = true;
				if(null != msg.obj && msg.obj instanceof String){
					new Thread(new MyRunnable((String)msg.obj)).start();
				}
				break;
			}
			
		}
	};
	class MyRunnable implements Runnable{
		private String path;
		MyRunnable(String p){
			path = p;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(null == path){
				return;
			}
			if(null == entity){
				return;
			}
			String str = entity.obtainLyricStr();
			if(TextUtils.isEmpty(str)){
				return;
			}
			BufferedWriter writer = null;
			OutputStreamWriter osw = null;
			FileOutputStream fos = null;
			try {
				File file = new File(path + ".tmp");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				fos = new FileOutputStream(file);
				osw = new OutputStreamWriter(fos, "UTF-8");
				writer = new BufferedWriter(osw);

				int start = 0;
				int len = 1024;
				while (start < str.length()) {
					if (!isRun) {
						return;
					}
					if (start + 1024 < str.length()) {
						len = 1024;
					} else {
						len = str.length() - start;
					}
					writer.write(str.substring(start, start + len));
					start += len;
				}
				file.renameTo(new File(path));
			} catch (IOException e) {

			} finally {
				try {
					if (null != writer) {
						writer.close();
					}
					if (null != osw) {
						osw.close();
					}
					if (null != fos) {
						fos.close();
					}
				} catch (IOException e) {
				}
				writer = null;
				osw = null;
				fos = null;
			}
		}
	}
	
	float mDownMotionX = 0.0f;
	float mDownMotionY = 0.0f;
	float mLastMotionX = 0.0f;
	float mLastMotionY = 0.0f;
	private float disY = 0.0f;
	private float disX = 0.0f;
	boolean isFirstLocation = false;
	private OnXGestureListener mOnXGestureListener = new OnXGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			if(null != onLyricEventListener){
				onLyricEventListener.onDown(LineByLineView.this);
			}
			if(touchmode == 2) {
				showTouchLine = false;
			}
			isFirstLocation = true;
			disX = 0.0f;
			disY = 0.0f;
			mDownMotionX = e.getX();
			mDownMotionY = e.getY();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			if (null != onLyricEventListener) {
				onLyricEventListener.onClick(LineByLineView.this);
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			float x = e2.getX();
			float y = e2.getY();
			disX += Math.abs(x - mDownMotionX);
			disY += Math.abs(y - mDownMotionY);
			if(disY < disX) {
				return false;
			}
			mDownMotionX = x;
			mDownMotionY = y;
			if(isFirstLocation) {
				isFirstLocation = false;
				mLastMotionX = x;
				mLastMotionY = y;
			}
			if(touchmode == 2) {
				if(!showTouchLine) {
					showTouchLine = true;
					invalidate();
					if(null != onLyricProgressListener){
						onLyricProgressListener.onStartTracking(LineByLineView.this);
					}
				}
			}
			float mMoveY = y - mLastMotionY;
			if(mMoveY<0){
				if(mOffsetY+mMoveY+entity.getSelfRect().height()<mDefaultOffsetY){
					mOffsetY = mDefaultOffsetY - entity.getSelfRect().height();
				}else{
					mOffsetY += mMoveY;
				}
			}else{
				if(mOffsetY + mMoveY > mDefaultOffsetY){
					mOffsetY = mDefaultOffsetY;
				}else{
					mOffsetY += mMoveY;
				}
			}
			rescroll();
			invalidate();
			mLastMotionX = x;
			mLastMotionY = y;
			if(touchmode == 2){
				int p = getHightLightRow();
				long time = entity.getOffsetTime();
				if(null != entity.getRowEntity(p)){
					time = entity.getRowEntity(p).getTime() - time;
				}
				entity.setHighlightRow(p);
				if(null != onLyricProgressListener){
					onLyricProgressListener.onHighLightChanged(LineByLineView.this,time,p);
				}
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onUp(MotionEvent e) {
			// TODO Auto-generated method stub
			if(touchmode == 2 && showTouchLine) {
				showTouchLine = false;
				invalidate();
				if(null != onLyricProgressListener){
					onLyricProgressListener.onStopTracking(LineByLineView.this);
				}
			}
			if(null != onLyricEventListener){
				onLyricEventListener.onUp(LineByLineView.this);
			}
			return true;
		}
		
	};
	
	public int getHignlightRowColor() {
		return mHignlightRowColor;
	}
	public void setHignlightRowColor(int mHignlightRowColor) {
		this.mHignlightRowColor = mHignlightRowColor;
		hPaint.setColor(mHignlightRowColor);
		postInvalidate();
	}
	public int getNormalRowColor() {
		return mNormalRowColor;
	}
	public void setNormalRowColor(int mNormalRowColor) {
		this.mNormalRowColor = mNormalRowColor;
		mPaint.setColor(mNormalRowColor);
		postInvalidate();
	}
	public int getFontSize() {
		return mFontSize;
	}
	public void setFontSize(int mFontSize) {
		this.mFontSize = mFontSize;
		hPaint.setTextSize(mFontSize);
		mPaint.setTextSize(mFontSize);
		measureAndLayout();
		postInvalidate();
	}
	public float getPaddingY() {
		return mPaddingY;
	}
	public void setPaddingY(float mPaddingY) {
		this.mPaddingY = mPaddingY;
	}
	public float getOffsetY() {
		return mOffsetY;
	}
	public float getDefaultOffsetY() {
		return mDefaultOffsetY;
	}
	public void setDefaultOffsetY(float dy) {
		mDefaultOffsetY = dy;
	}
	public void setOffsetY(float mOffsetY) {
		this.mOffsetY = mOffsetY;
	}
	
	public float getSrcOffsetY() {
		return mSrcOffsetY;
	}
	public int getScrollMode() {
		return scrollmode;
	}
	public void setScrollMode(int scrollmode) {
		this.scrollmode = scrollmode;
	}
	
	public int getTouchMode() {
		return touchmode;
	}
	public void setTouchMode(int touchmode) {
		this.touchmode = touchmode;
	}
	public boolean isShadowable() {
		return shadowable;
	}
	
	public int getTouchLineColor() {
		return touchLineColor;
	}
	public void setTouchLineColor(int touchLineColor) {
		this.touchLineColor = touchLineColor;
	}
	public int getTouchLineHeight() {
		return touchLineHeight;
	}
	public void setTouchLineHeight(int touchLineHeight) {
		this.touchLineHeight = touchLineHeight;
	}
	public void setShadowable(boolean shadowable) {
		this.shadowable = shadowable;
		if(shadowable){
			if(null == shadowPaint){
				shadowPaint = new Paint();
			}
		}else{
			if( null  != shadowPaint){
				shadowPaint.setShader(null);
			}
			shadowPaint = null;
		}
		shadowGradientUp = null;
		shadowGradientDown = null;
	}
	public int getShadowHeight() {
		return shadowHeight;
	}
	public void setShadowHeight(int shadowHeight) {
		this.shadowHeight = shadowHeight;
	}
	public int getShadowFrontColor() {
		return shadowFrontColor;
	}
	public void setShadowFrontColor(int shadowFrontColor) {
		this.shadowFrontColor = shadowFrontColor;
	}
	public int getShadowEndColor() {
		return shadowEndColor;
	}
	public void setShadowEndColor(int shadowEndColor) {
		this.shadowEndColor = shadowEndColor;
	}
	public OnLyricEventListener getOnLyricEventListener() {
		return onLyricEventListener;
	}
	public void setOnLyricEventListener(OnLyricEventListener onLyricEventListener) {
		this.onLyricEventListener = onLyricEventListener;
	}
	public OnLyricProgressListener getOnLyricProgressListener() {
		return onLyricProgressListener;
	}
	public void setOnLyricProgressListener(
			OnLyricProgressListener onLyricProgressListener) {
		this.onLyricProgressListener = onLyricProgressListener;
	}
	
}
