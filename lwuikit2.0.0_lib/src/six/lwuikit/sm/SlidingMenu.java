package six.lwuikit.sm;

import java.lang.reflect.Method;

import six.lwuikit.saf.RE;
import six.lwuikit.sm.ViewAbove.OnPageChangeListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SlidingMenu extends RelativeLayout{
	
	public static final int SLIDING_WINDOW = 0;
	public static final int SLIDING_CONTENT = 1;
	
	private boolean mActionbarOverlay = true;
	
	public static final int TOUCHMODE_MARGIN = 0;
	public static final int TOUCHMODE_FULLSCREEN = 1;
	public static final int TOUCHMODE_NONE = 2;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int LEFT_RIGHT = 2;

	private ViewAbove mViewAbove;
	private ViewBehind mViewBehind;

	private OnOpenListener mOpenListener;
	private OnCloseListener mCloseListener;
	
	public SlidingMenu(Context context) {
		this(context, null);
	}

	public SlidingMenu(Activity activity, int slideStyle) {
		this(activity, null);
		this.attachToActivity(activity, slideStyle);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewBehind = new ViewBehind(context);
		addView(mViewBehind, behindParams);
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mViewAbove = new ViewAbove(context);
		addView(mViewAbove, aboveParams);
		mViewAbove.setViewBehind(mViewBehind);
		mViewBehind.setViewAbove(mViewAbove);
		mViewAbove.setOnPageChangeListener(new OnPageChangeListener() {
			public static final int POSITION_OPEN = 0;
			public static final int POSITION_CLOSE = 1;

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) { }

			public void onPageSelected(int position) {
				if (position == POSITION_OPEN && mOpenListener != null) {
					mOpenListener.onOpen();
				} else if (position == POSITION_CLOSE && mCloseListener != null) {
					mCloseListener.onClose();
				}
			}
		});
		int offsetBehind = -1;
		int widthBehind = -1;
		TypedArray a = context.obtainStyledAttributes(attrs, RE.styleable_SlidingMenu);
		if(null != a){
			int index = RE.get(RE.ATTR_MODE);
			if(index >= 0){
				int mode = a.getInt(index, LEFT);
				setMode(mode);
			}
			index = RE.get(RE.ATTR_VIEWABOVE);
			if(index >= 0){
				int viewAbove = a.getResourceId(index, -1);
				if (viewAbove != -1) {
					setContent(viewAbove);
				} else {
					setContent(new FrameLayout(context));
				}
			}
			index = RE.get(RE.ATTR_VIEWBEHIND);
			if(index >= 0){
				int viewBehind = a.getResourceId(index, -1);
				if (viewBehind != -1) {
					setMenu(viewBehind); 
				} else {
					setMenu(new FrameLayout(context));
				}
			}
			index = RE.get(RE.ATTR_TOUCHMODEABOVE);
			if(index >= 0){
				int touchModeAbove = a.getInt(index, TOUCHMODE_MARGIN);
				setTouchModeAbove(touchModeAbove);
			}
			index = RE.get(RE.ATTR_TOUCHMODEBEHIND);
			if(index >= 0){
				int viewBehind = a.getResourceId(index, -1);
				if (viewBehind != -1) {
					setMenu(viewBehind); 
				} else {
					setMenu(new FrameLayout(context));
				}
			}
			index = RE.get(RE.ATTR_BEHINDOFFSET);
			if(index >= 0){
				offsetBehind = (int) a.getDimension(index, -1);
			}
			index = RE.get(RE.ATTR_BEHINDWIDTH);
			if(index >= 0){
				widthBehind = (int) a.getDimension(index, -1);
			}
			index = RE.get(RE.ATTR_BEHINDSCROLLSCALE);
			if(index >= 0){
				float scrollOffsetBehind = a.getFloat(index, 0.33f);
				setBehindScrollScale(scrollOffsetBehind);
			}
			index = RE.get(RE.ATTR_SHADOWDRAWABLE);
			if(index >= 0){
				int shadowRes = a.getResourceId(index, -1);
				if (shadowRes != -1) {
					setShadowDrawable(shadowRes);
				}
			}
			index = RE.get(RE.ATTR_SHADOWWIDTH);
			if(index >= 0){
				int shadowWidth = (int) a.getDimension(index, 0);
				setShadowWidth(shadowWidth);
			}
			index = RE.get(RE.ATTR_FADEENABLED);
			if(index >= 0){
				boolean fadeEnabled = a.getBoolean(index, true);
				setFadeEnabled(fadeEnabled);
			}
			index = RE.get(RE.ATTR_FADEDEGREE);
			if(index >= 0){
				float fadeDeg = a.getFloat(index, 0.33f);
				setFadeDegree(fadeDeg);
			}
			a.recycle();
		}
		if (offsetBehind != -1 && widthBehind != -1)
			throw new IllegalStateException("Cannot set both behindOffset and behindWidth for a SlidingMenu");
		else if (offsetBehind != -1)
			setBehindOffset(offsetBehind);
		else if (widthBehind != -1)
			setBehindWidth(widthBehind);
		else
			setBehindOffset(0);
	}
	
	public void attachToActivity(Activity activity, int slideStyle) {
		attachToActivity(activity, slideStyle, false);
	}

	public void attachToActivity(Activity activity, int slideStyle, boolean actionbarOverlay) {
		if (slideStyle != SLIDING_WINDOW && slideStyle != SLIDING_CONTENT)
			throw new IllegalArgumentException("slideStyle must be either SLIDING_WINDOW or SLIDING_CONTENT");

		if (getParent() != null)
			throw new IllegalStateException("This SlidingMenu appears to already be attached");

		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);
		a.recycle();

		switch (slideStyle) {
		case SLIDING_WINDOW:
			mActionbarOverlay = false;
			ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			decorChild.setBackgroundResource(background);
			decor.removeView(decorChild);
			decor.addView(this);
			setContent(decorChild);
			break;
		case SLIDING_CONTENT:
			mActionbarOverlay = actionbarOverlay;
			ViewGroup contentParent = (ViewGroup)activity.findViewById(android.R.id.content);
			View content = contentParent.getChildAt(0);
			contentParent.removeView(content);
			contentParent.addView(this);
			setContent(content);
			if (content.getBackground() == null)
				content.setBackgroundResource(background);
			break;
		}
	}

	public void setContent(int res) {
		setContent(LayoutInflater.from(getContext()).inflate(res, null));
	}
	
	public void setContent(View view) {
		mViewAbove.setContent(view);
		showContent();
	}

	public View getContent() {
		return mViewAbove.getContent();
	}

	public void setMenu(int res) {
		setMenu(LayoutInflater.from(getContext()).inflate(res, null));
	}

	public void setMenu(View v) {
		mViewBehind.setContent(v);
	}

	public View getMenu() {
		return mViewBehind.getContent();
	}

	public void setSecondaryMenu(int res) {
		setSecondaryMenu(LayoutInflater.from(getContext()).inflate(res, null));
	}

	public void setSecondaryMenu(View v) {
		mViewBehind.setSecondaryContent(v);
	}

	public View getSecondaryMenu() {
		return mViewBehind.getSecondaryContent();
	}


	public void setSlidingEnabled(boolean b) {
		mViewAbove.setSlidingEnabled(b);
	}

	public boolean isSlidingEnabled() {
		return mViewAbove.isSlidingEnabled();
	}

	public void setMode(int mode) {
		if (mode != LEFT && mode != RIGHT && mode != LEFT_RIGHT) {
			throw new IllegalStateException("SlidingMenu mode must be LEFT, RIGHT, or LEFT_RIGHT");
		}
		mViewBehind.setMode(mode);
	}

	public int getMode() {
		return mViewBehind.getMode();
	}

	public void setStatic(boolean b) {
		if (b) {
			setSlidingEnabled(false);
			mViewAbove.setViewBehind(null);
			mViewAbove.setCurrentItem(1);
		} else {
			mViewAbove.setCurrentItem(1);
			mViewAbove.setViewBehind(mViewBehind);
			setSlidingEnabled(true);
		}
	}

	public void showMenu() {
		showMenu(true);
	}

	public void showMenu(boolean animate) {
		mViewAbove.setCurrentItem(0, animate);
	}

	public void showSecondaryMenu() {
		showSecondaryMenu(true);
	}

	public void showSecondaryMenu(boolean animate) {
		mViewAbove.setCurrentItem(2, animate);
	}

	public void showContent() {
		showContent(true);
	}

	
	public void showContent(boolean animate) {
		mViewAbove.setCurrentItem(1, animate);
	}

	public void toggle() {
		toggle(true);
	}

	public void toggle(boolean animate) {
		if (isMenuShowing()) {
			showContent(animate);
		} else {
			showMenu(animate);
		}
	}

	public boolean isMenuShowing() {
		return mViewAbove.getCurrentItem() == 0 || mViewAbove.getCurrentItem() == 2;
	}
	
	
	public boolean isSecondaryMenuShowing() {
		return mViewAbove.getCurrentItem() == 2;
	}

	public int getBehindOffset() {
		return ((RelativeLayout.LayoutParams)mViewBehind.getLayoutParams()).rightMargin;
	}

	
	public void setBehindOffset(int i) {
		mViewBehind.setWidthOffset(i);
	}

	public void setBehindOffsetRes(int resID) {
		int i = (int) getContext().getResources().getDimension(resID);
		setBehindOffset(i);
	}

	public void setAboveOffset(int i) {
		mViewAbove.setAboveOffset(i);
	}

	
	public void setAboveOffsetRes(int resID) {
		int i = (int) getContext().getResources().getDimension(resID);
		setAboveOffset(i);
	}

	@SuppressWarnings("deprecation")
	public void setBehindWidth(int i) {
		int width;
		Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		try {
			Class<?> cls = Display.class;
			Class<?>[] parameterTypes = {Point.class};
			Point parameter = new Point();
			Method method = cls.getMethod("getSize", parameterTypes);
			method.invoke(display, parameter);
			width = parameter.x;
		} catch (Exception e) {
			width = display.getWidth();
		}
		setBehindOffset(width-i);
	}

	public void setBehindWidthRes(int res) {
		int i = (int) getContext().getResources().getDimension(res);
		setBehindWidth(i);
	}

	
	public float getBehindScrollScale() {
		return mViewBehind.getScrollScale();
	}
	
	public int getTouchmodeMarginThreshold() {
		return mViewBehind.getMarginThreshold();
	}
	
	public void setTouchmodeMarginThreshold(int touchmodeMarginThreshold) {
		mViewBehind.setMarginThreshold(touchmodeMarginThreshold);
	}

	public void setBehindScrollScale(float f) {
		if (f < 0 && f > 1)
			throw new IllegalStateException("ScrollScale must be between 0 and 1");
		mViewBehind.setScrollScale(f);
	}

	public void setBehindCanvasTransformer(CanvasTransformer t) {
		mViewBehind.setCanvasTransformer(t);
	}

	
	public int getTouchModeAbove() {
		return mViewAbove.getTouchMode();
	}

	public void setTouchModeAbove(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN
				&& i != TOUCHMODE_NONE) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
		}
		mViewAbove.setTouchMode(i);
	}

	public void setTouchModeBehind(int i) {
		if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN
				&& i != TOUCHMODE_NONE) {
			throw new IllegalStateException("TouchMode must be set to either" +
					"TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
		}
		mViewBehind.setTouchMode(i);
	}

	public void setShadowDrawable(int resId) {
		setShadowDrawable(getContext().getResources().getDrawable(resId));
	}

	
	public void setShadowDrawable(Drawable d) {
		mViewBehind.setShadowDrawable(d);
	}

	public void setSecondaryShadowDrawable(int resId) {
		setSecondaryShadowDrawable(getContext().getResources().getDrawable(resId));
	}

	
	public void setSecondaryShadowDrawable(Drawable d) {
		mViewBehind.setSecondaryShadowDrawable(d);
	}

	public void setShadowWidthRes(int resId) {
		setShadowWidth((int)getResources().getDimension(resId));
	}

	public void setShadowWidth(int pixels) {
		mViewBehind.setShadowWidth(pixels);
	}

	
	public void setFadeEnabled(boolean b) {
		mViewBehind.setFadeEnabled(b);
	}

	public void setFadeDegree(float f) {
		mViewBehind.setFadeDegree(f);
	}

	
	public void addIgnoredView(View v) {
		mViewAbove.addIgnoredView(v);
	}

	public void removeIgnoredView(View v) {
		mViewAbove.removeIgnoredView(v);
	}

	public void clearIgnoredViews() {
		mViewAbove.clearIgnoredViews();
	}

	public void setOnOpenListener(OnOpenListener listener) {
		mOpenListener = listener;
	}

	public void setOnCloseListener(OnCloseListener listener) {
		mCloseListener = listener;
	}

	public void setOnOpenedListener(OnOpenedListener listener) {
		mViewAbove.setOnOpenedListener(listener);
	}

	public void setOnClosedListener(OnClosedListener listener) {
		mViewAbove.setOnClosedListener(listener);
	}

	public static class SavedState extends BaseSavedState {

		private final int mItem;

		public SavedState(Parcelable superState, int item) {
			super(superState);
			mItem = item;
		}

		private SavedState(Parcel in) {
			super(in);
			mItem = in.readInt();
		}

		public int getItem() {
			return mItem;
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(mItem);
		}

		public static final Parcelable.Creator<SavedState> CREATOR =
				new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState, mViewAbove.getCurrentItem());
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		mViewAbove.setCurrentItem(ss.getItem());
	}

	@SuppressLint("NewApi")
	@Override
	protected boolean fitSystemWindows(Rect insets) {
		int leftPadding = insets.left;
		int rightPadding = insets.right;
		int topPadding = insets.top;
		int bottomPadding = insets.bottom;
		if (!mActionbarOverlay) {
			setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
		}
		return true;
	}
	
	private Handler mHandler = new Handler();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void manageLayers(float percentOpen) {
		if (Build.VERSION.SDK_INT < 11) return;

		boolean layer = percentOpen > 0.0f && percentOpen < 1.0f;
		final int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;

		if (layerType != getContent().getLayerType()) {
			mHandler.post(new Runnable() {
				public void run() {
					getContent().setLayerType(layerType, null);
					getMenu().setLayerType(layerType, null);
					if (getSecondaryMenu() != null) {
						getSecondaryMenu().setLayerType(layerType, null);
					}
				}
			});
		}
	}
	
	//************************************************************
	//               *--自定义接口--*
	
	public interface CanvasTransformer {
		public void transformCanvas(Canvas canvas, float percentOpen);
	}
	
	public interface OnOpenListener {
		public void onOpen();
	}
	
	public interface OnOpenedListener {
		public void onOpened();
	}
	
	public interface OnCloseListener {
		public void onClose();
	}
	
	public interface OnClosedListener {
		public void onClosed();
	}
}
