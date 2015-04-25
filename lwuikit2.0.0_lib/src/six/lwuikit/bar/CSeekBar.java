package six.lwuikit.bar;

import six.lwuikit.saf.RE;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CSeekBar extends View {

	private Context mContext;
	private OnSeekChangeListener mListener;
	private Paint circleColor;
	private Paint backgroundPaint;
	private boolean backgroundable = false;
	private int circleBackColor = Color.GRAY;
	private float angle = 45.1f;
	private int barWidth = 5;
	private int maxProgress = 100;
	private int progress=10;
	private int mRevolutions = 0;
	private float outerRadius, cx, cy, gx, gy ,interRadius;
	private Bitmap progressMark,ic_jian,ic_square,ic_square_pink,ic_jia;
	private boolean IS_CROSSED = false;
	private boolean CALLED_FROM_ANGLE = false;
	private float degrees;
	private float mOldX;
	private int size;
	private float outSideRadius;
	{
		mListener = new OnSeekChangeListener() {
			@Override
			public void onProgressChange(CSeekBar view, int newProgress) {

			}

			@Override
			public void onProgressChangexxx(CSeekBar view,
					int newProgress, int xxx) {
			}

			@Override
			public void onStartTrackingTouch(CSeekBar view) {
				
			}

			@Override
			public void onStopTrackingTouch(CSeekBar view) {
				
			}
		};
		
		circleColor = new Paint();
		circleColor.setColor(Color.GREEN);
		circleColor.setAntiAlias(true);
		circleColor.setStrokeWidth(50);
		circleColor.setStyle(Paint.Style.FILL);
		
		backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setStyle(Paint.Style.FILL);
	}

	public CSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs, RE.styleable_CSeekBar);
		if(null != a){
			int index = RE.get(RE.ATTR_POINTDRAWABLE);
			if(index >= 0){
				progressMark = BitmapFactory.decodeResource(mContext.getResources(),a.getResourceId(index, -1));
			}
			index = RE.get(RE.ATTR_SQUAREDRAWABLE);
			if(index >= 0){
				ic_square = BitmapFactory.decodeResource(mContext.getResources(),a.getResourceId(index, -1));
			}
			index = RE.get(RE.ATTR_PINKDRAWABLE);
			if(index >= 0){
				ic_square_pink = BitmapFactory.decodeResource(mContext.getResources(),a.getResourceId(index, -1));
			}
			index = RE.get(RE.ATTR_SIGNDRAWABLE);
			if(index >= 0){
				ic_jia = BitmapFactory.decodeResource(mContext.getResources(),a.getResourceId(index, -1));
			}
			index = RE.get(RE.ATTR_UNSIGNDRAWABLE);
			if(index >= 0){
				ic_jian = BitmapFactory.decodeResource(mContext.getResources(),a.getResourceId(index, -1));
			}
			index = RE.get(RE.ATTR_BACKGROUNDABLE);
			if(index >= 0){
				setBackgroundable(a.getBoolean(index, false));
			}
			index = RE.get(RE.ATTR_CIRCLEBACKCOLOR);
			if(index >= 0){
				setCircleBackColor(a.getColor(index, Color.GRAY));
			}
			a.recycle();
		}
	}

	public CSeekBar(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public CSeekBar(Context context) {
		this(context,null,0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getWidth(); 
		int height = getHeight(); 
		size = (width > height) ? height : width;
		outSideRadius = (float) (size / 2.0f );
		cx = width / 2.0f;
		cy = height / 2.0f;
		outerRadius = (float) (size / 2.0f*0.94f);//0.94个半径那么宽
		interRadius = outerRadius * 0.95f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(backgroundable){
			canvas.drawCircle(cx, cy, interRadius, backgroundPaint);
		}
		drawMarkerAtProgress(canvas);
		super.onDraw(canvas);
	}
	
	private void drawMarkerAtProgress(Canvas canvas) {
		getGuidePosition();//获取到gx  gy  的值
		drawPointWithWhite(canvas);
		
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		matrix.postTranslate(gx, gy);
		canvas.drawBitmap(progressMark, matrix, null);
		
		drawPointWithRed(canvas,angle);
	}
	private void drawPointWithRed(Canvas canvas,float angle){
			if(angle>61&&angle<315){
				int numbers=(int)((angle-55)/10);
				
				for (int i=0; i < numbers+1; i++) {
					double triangle_angle = Math.atan(4 / outSideRadius)* 180 / Math.PI;
					float myx,myy;
					myx = cx+ (float) (-(outSideRadius * Math.sin((55+i*10 + triangle_angle)* (Math.PI) / 180))); 
					myy = cy- (float) (-(outSideRadius * Math.cos((55+i*10 + triangle_angle)* (Math.PI) / 180)));
					Matrix matrix4= new Matrix();
					matrix4.postTranslate(myx, myy);
					canvas.drawBitmap(ic_square_pink, matrix4, null);
				}
			}

	}
	private void drawPointWithWhite(Canvas canvas){
		Matrix matrix1 = new Matrix();
		matrix1.postTranslate(cx*0.2f, cy*1.7f);
		canvas.drawBitmap(ic_jian, matrix1, null);
		
		Matrix matrix2 = new Matrix();
		matrix2.postTranslate(cx*1.7f, cy*1.7f);
		canvas.drawBitmap(ic_jia, matrix2, null);
		
		//--------------
		for (int myangle= 55; myangle < 306; myangle=myangle+10) {
			double triangle_angle = Math.atan(4 / outSideRadius)* 180 / Math.PI;
			float myx,myy;
			myx = cx+ (float) (-(outSideRadius * Math.sin((myangle + triangle_angle)* (Math.PI) / 180))); 
			myy = cy- (float) (-(outSideRadius * Math.cos((myangle + triangle_angle)* (Math.PI) / 180)));
			Matrix matrix3 = new Matrix();
			matrix3.postTranslate(myx, myy);
			canvas.drawBitmap(ic_square, matrix3, null);
		}
		
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
		float donePercent = (((float) this.angle) / 360) * 100;
		float progressValue = (donePercent / 100) * getMaxProgress();
		CALLED_FROM_ANGLE = true;
		setProgress(Math.round(progressValue));
	}

	private void getGuidePosition() {
		float pointRadius = (float) (outerRadius *0.6);
		float triangle_size = 8;
		double triangle_angle = Math.atan((triangle_size * 0.5) / pointRadius)* 180 / Math.PI;// 15
			gx = cx
					+ (float) (-(pointRadius * Math.sin((angle + triangle_angle)
							* (Math.PI) / 180))); // +15
			gy = cy
					- (float) (-(pointRadius * Math.cos((angle + triangle_angle)
							* (Math.PI) / 180)));// +15
		if(angle>=314.0f){
			angle = 314.0f;
			gx = cx
					+ (float) (-(pointRadius * Math.sin((angle + triangle_angle)
							* (Math.PI) / 180))); // +15
			gy = cy
					- (float) (-(pointRadius * Math.cos((angle + triangle_angle)
							* (Math.PI) / 180)));// +15
		}
		if(angle<=58.0f){
			angle=58.0f;
			gx = cx
					+ (float) (-(pointRadius * Math.sin((angle + triangle_angle)
							* (Math.PI) / 180))); // +15
			gy = cy
					- (float) (-(pointRadius * Math.cos((angle + triangle_angle)
							* (Math.PI) / 180)));// +15
		}
		
	}

	public void setSeekBarChangeListener(OnSeekChangeListener listener) {
		mListener = listener;
	}

	public OnSeekChangeListener getSeekBarChangeListener() {
		return mListener;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public interface OnSeekChangeListener {
		void onProgressChange(CSeekBar view, int newProgress);

		void onProgressChangexxx(CSeekBar view, int newProgress, int i);
		
		void onStartTrackingTouch(CSeekBar view);

        void onStopTrackingTouch(CSeekBar view);
	}
	
	public boolean isBackgroundable() {
		return backgroundable;
	}

	public void setBackgroundable(boolean backgroundable) {
		this.backgroundable = backgroundable;
	}

	public int getCircleBackColor() {
		return circleBackColor;
	}

	public void setCircleBackColor(int backgroundColor) {
		this.circleBackColor = backgroundColor;
		backgroundPaint.setColor(backgroundColor);
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		if (this.progress != progress) {
			this.progress = progress;
			if (!CALLED_FROM_ANGLE) {
				int newPercent = (this.progress / this.maxProgress) * 100;
				int newAngle = (newPercent / 100) * 360;

				this.setAngle(newAngle);
			}
			mListener.onProgressChange(this, this.getProgress());
			mListener.onProgressChangexxx(this, this.getProgress(), 2);
			CALLED_FROM_ANGLE = false;
		}
	}
	public void setProgressColor(int color) {
		circleColor.setColor(color);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x2 = event.getX() - cx;
		float y2 = cy - event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mOldX = x2;
			mRevolutions = 0;
			if( mListener != null ) {
				mListener.onStartTrackingTouch(this);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			updateRevolutions(x2, y2);
			moved(x2, y2);
			if (!IS_CROSSED) {
				setAngle(Math.round(degrees));
				CALLED_FROM_ANGLE = true;
				setProgress(progress);
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if( mListener != null ) {
				mListener.onStopTrackingTouch(this);
			}
			IS_CROSSED = false;
			break;
		default:
			break;
		}
		mOldX = x2;
		return true;

	}

	private void moved(float x, float y) {
		degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x, y)) + 180.0)) % 360.0);// +360
		float position = degrees / 360;
		progress = (int) (position * 100);
		float absPosition = (float) mRevolutions + position;
		if (absPosition < 0.0f)
			progress = 0;
		else if (absPosition > 1.0f)
			progress = maxProgress;
	}

	private void updateRevolutions(float x, float y) {
		boolean leftFlip = y < 0.0f && (mOldX <= 0.0f && x > 0.0f);
		boolean rightFlip = y < 0.0f && (mOldX >= 0.0f && x < 0.0f);

		if (leftFlip)
			mRevolutions -= 1.0f;
		else if (rightFlip)
			mRevolutions += 1.0f;
		mRevolutions = Math.max(-1, Math.min(1, mRevolutions));
		if (mRevolutions == 1 || mRevolutions == -1) {
			this.IS_CROSSED = true;
		} else {
			this.IS_CROSSED = false;

		}
	}
	
	public void moveToProgress(int setProgress){
		if( setProgress < 0  || setProgress > getMaxProgress() ) {
			return;
		}
		
		float setAngles = ((float)setProgress) / getMaxProgress() * 360.0f;
		setAngle(Math.round(setAngles));
		CALLED_FROM_ANGLE = true;
		setProgress(setProgress);
		invalidate();
   }
}