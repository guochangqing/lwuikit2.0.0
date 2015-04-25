package six.lwuikit.app.view;

import six.lwuikit.R;
import six.lwuikit.eco.EcoGallery;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;


public class CustomGallery extends EcoGallery{

	public static final int SDKVERSION = 15;
	private Camera mCamera = new Camera();
	
	private float offsetx = 50.0f;
	
	public void setOffsetx(float x){
		offsetx = x;
	}
	
	Matrix mMatrix = new Matrix();
	public CustomGallery(Context context){
		super(context);
		this.setStaticTransformationsEnabled(true);  
	}
	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setStaticTransformationsEnabled(true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomGallery);
		float index = a.getDimension(R.styleable.CustomGallery_offsetx, 50.0f);
		setOffsetx(index);
		a.recycle();
	}
	
	 
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		if (android.os.Build.VERSION.SDK_INT > SDKVERSION) {
			return false;
		} else {
			t.clear();
			t.setTransformationType(Transformation.TYPE_MATRIX);
			final float offset = calculateOffsetOfCenter(child);
			transformViewRoom(child, t, offset);
			return true;
		}
	}
	@Override  
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {  
	    // TODO Auto-generated method stub  
	    boolean ret;  
	    //Android SDK 4.1  
	    if(android.os.Build.VERSION.SDK_INT > SDKVERSION){  
	        final float offset = calculateOffsetOfCenter(child);  
	        getTransformationMatrix(child, offset);  
	          
//	        child.setAlpha(1 - Math.abs(offset));  
	        final int saveCount = canvas.save();  
	        canvas.concat(mMatrix);  
	        ret = super.drawChild(canvas, child, drawingTime);  
	        canvas.restoreToCount(saveCount);  
	    }else{  
	        ret = super.drawChild(canvas, child, drawingTime);  
	    }  
	    return ret;  
	}  
	
	void getTransformationMatrix(View child, float offset) {  
	    final int halfWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);  
	    final int halfHeight = child.getMeasuredHeight() >> 1;  
	      
	    mCamera.save();  
	    mCamera.translate(-offset * offsetx, 0.0f , Math.abs(offset) * 200);  
	      
	    mCamera.getMatrix(mMatrix);  
	    mCamera.restore();  
	    mMatrix.preTranslate(-halfWidth, -halfHeight);  
	    mMatrix.postTranslate(halfWidth, halfHeight);
	}  
	
	//获取父控件中心点 X 的位置  
	protected int getCenterOfCoverflow() {  
	        return ((getWidth() - getPaddingLeft() - getPaddingRight()) >> 1) + getPaddingLeft();  
	}  
	//获取 child 中心点 X 的位置  
	protected  int getCenterOfView(View view) {  
	        return view.getLeft() + (view.getWidth() >> 1);  
	}  
	//计算 child 偏离 父控件中心的 offset 值， -1 <= offset <= 1  
	protected float calculateOffsetOfCenter(View view){  
	    final int pCenter = getCenterOfCoverflow();  
	    final int cCenter = getCenterOfView(view);  
	  
	    float offset = (cCenter - pCenter) / (pCenter * 1.0f);  
	    offset = Math.min(offset, 1.0f);  
	    offset = Math.max(offset, -1.0f);  
	  
	    return offset;  
	}  
	void transformViewRoom(View child, Transformation t, float race){  
	    mCamera.save();  
	    final Matrix matrix = t.getMatrix();  
	    final int halfHeight = child.getMeasuredHeight() >> 1;  
	    final int halfWidth = child.getMeasuredWidth() >> 1;  
	  
	    // 平移 X、Y、Z 轴已达到立体效果  
	    mCamera.translate(-race * offsetx, 0.0f , Math.abs(race) * 200);  
	    //也可设置旋转效果  
	    mCamera.getMatrix(matrix);  
	    //以 child 的中心点变换  
	    matrix.preTranslate(-halfWidth, -halfHeight);  
	    matrix.postTranslate(halfWidth, halfHeight);  
	    mCamera.restore();  
	    //设置 alpha 变换  
//	    t.setAlpha(1 - Math.abs(race));  
	}  
	
}
