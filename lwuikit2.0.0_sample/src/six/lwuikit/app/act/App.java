package six.lwuikit.app.act;

import six.lwuikit.R;
import six.lwuikit.saf.RE;
import six.lwuikit.tip.Tip;
import android.app.Application;
import android.graphics.Color;

public class App extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		// Configuration
		Tip.makeTip().init(this);
		Tip.makeTip().setBackground(Color.RED);
		Tip.makeTip().setTipHeight(300);
		
		RE.setStyleableSaf(R.styleable.saf);
		RE.put(RE.ATTR_AEVENT, R.styleable.saf_aEvent);
		
		RE.setStyleableCSeekBar(R.styleable.CSeekBar);
		RE.put(RE.ATTR_CIRCLEBACKCOLOR, R.styleable.CSeekBar_circleBackColor);
		RE.put(RE.ATTR_BACKGROUNDABLE, R.styleable.CSeekBar_backgroundable);
		RE.put(RE.ATTR_POINTDRAWABLE, R.styleable.CSeekBar_pointDrawable);
		RE.put(RE.ATTR_SQUAREDRAWABLE, R.styleable.CSeekBar_squareDrawable);
		RE.put(RE.ATTR_PINKDRAWABLE, R.styleable.CSeekBar_pinkDrawable);
		RE.put(RE.ATTR_SIGNDRAWABLE, R.styleable.CSeekBar_signDrawable);
		RE.put(RE.ATTR_UNSIGNDRAWABLE, R.styleable.CSeekBar_unsignDrawable);
		
		RE.setStyleableIProgressBar(R.styleable.IProgressBar);
		RE.put(RE.ATTR_FRAMESDURATION, R.styleable.IProgressBar_framesDuration);
		RE.put(RE.ATTR_FRAMESCOUNT, R.styleable.IProgressBar_framesCount);
		
		RE.setStyleableProgressBar(R.styleable.ProgressBar);
		RE.put(RE.ATTR_ANDROID_PROGRESSDRAWABLE, R.styleable.ProgressBar_android_progressDrawable);
		RE.put(RE.ATTR_ANDROID_MINWIDTH, R.styleable.ProgressBar_android_minWidth);
		RE.put(RE.ATTR_ANDROID_MAXWIDTH, R.styleable.ProgressBar_android_maxWidth);
		RE.put(RE.ATTR_ANDROID_MINHEIGHT, R.styleable.ProgressBar_android_minHeight);
		RE.put(RE.ATTR_ANDROID_MAXHEIGHT, R.styleable.ProgressBar_android_maxHeight);
		RE.put(RE.ATTR_ANDROID_MAX, R.styleable.ProgressBar_android_max);
		RE.put(RE.ATTR_ANDROID_PADDINGLEFT, R.styleable.ProgressBar_android_paddingLeft);
		RE.put(RE.ATTR_ANDROID_PADDINGRIGHT, R.styleable.ProgressBar_android_paddingRight);
		RE.put(RE.ATTR_ANDROID_PROGRESS, R.styleable.ProgressBar_android_progress);
		RE.put(RE.ATTR_ANDROID_SECONDARYPROGRESS, R.styleable.ProgressBar_android_secondaryProgress);
		
		RE.setStyleableSeekBar(R.styleable.SeekBar);
		RE.put(RE.ATTR_ANDROID_THUMB, R.styleable.SeekBar_android_thumb);
		RE.put(RE.ATTR_ANDROID_THUMBOFFSET, R.styleable.SeekBar_android_thumbOffset);
		RE.put(RE.ATTR_ANDROID_DISABLEDALPHA, R.styleable.SeekBar_android_disabledAlpha);
		
		RE.setStyleableWheelView(R.styleable.WheelView);
		RE.put(RE.ATTR_WV_TEXTCOLOR, R.styleable.WheelView_wv_TextColor);
		RE.put(RE.ATTR_WV_TEXTFOCUSEDCOLOR, R.styleable.WheelView_wv_TextFocusedColor);
		RE.put(RE.ATTR_WV_LONGSCALELINECOLOR, R.styleable.WheelView_wv_LongScaleLineColor);
		RE.put(RE.ATTR_WV_LONGSCALELINEFOCUSEDCOLOR, R.styleable.WheelView_wv_LongScaleLineFocusedColor);
		RE.put(RE.ATTR_WV_SCALELENGTHSHORT, R.styleable.WheelView_wv_ScaleLengthShort);
		RE.put(RE.ATTR_WV_MARGINTOPOFLABEL, R.styleable.WheelView_wv_MarginTopOfLabel);
		RE.put(RE.ATTR_WV_LONGSCALEWIDTH, R.styleable.WheelView_wv_LongScaleWidth);
		RE.put(RE.ATTR_WV_TEXTSIZE, R.styleable.WheelView_wv_TextSize);
		RE.put(RE.ATTR_WV_HORIZONTAL, R.styleable.WheelView_wv_Horizontal);
		
		RE.setStyleableEcoGallery(R.styleable.EcoGallery);
		RE.put(RE.ATTR_GRAVITYS, R.styleable.EcoGallery_gravitys);
		RE.put(RE.ATTR_ANIMATIONDURATION, R.styleable.EcoGallery_animationDuration);
		RE.put(RE.ATTR_SPACING, R.styleable.EcoGallery_spacing);
		RE.put(RE.ATTR_UNSELECTEDALPHA, R.styleable.EcoGallery_unselectedAlpha);
		
		RE.setStyleableCustomAbsSpinner(R.styleable.CustomAbsSpinner);
		RE.put(RE.ATTR_ENTRIES, R.styleable.CustomAbsSpinner_entries);
		
		RE.setStyleableSlidingMenu(R.styleable.SlidingMenu);
		RE.put(RE.ATTR_MODE, R.styleable.SlidingMenu_mode);
		RE.put(RE.ATTR_VIEWABOVE, R.styleable.SlidingMenu_viewAbove);
		RE.put(RE.ATTR_VIEWBEHIND, R.styleable.SlidingMenu_viewBehind);
		RE.put(RE.ATTR_TOUCHMODEABOVE, R.styleable.SlidingMenu_touchModeAbove);
		RE.put(RE.ATTR_TOUCHMODEBEHIND, R.styleable.SlidingMenu_touchModeBehind);
		RE.put(RE.ATTR_BEHINDOFFSET, R.styleable.SlidingMenu_behindOffset);
		RE.put(RE.ATTR_BEHINDWIDTH, R.styleable.SlidingMenu_behindWidth);
		RE.put(RE.ATTR_BEHINDSCROLLSCALE, R.styleable.SlidingMenu_behindScrollScale);
		RE.put(RE.ATTR_SHADOWDRAWABLE, R.styleable.SlidingMenu_shadowDrawable);
		RE.put(RE.ATTR_SHADOWWIDTH, R.styleable.SlidingMenu_shadowWidth);
		RE.put(RE.ATTR_FADEENABLED, R.styleable.SlidingMenu_fadeEnabled);
		RE.put(RE.ATTR_FADEDEGREE, R.styleable.SlidingMenu_fadeDegree);
		
		RE.setStyleableAlphabetView(R.styleable.AlphabetView);
		RE.put(RE.ATTR_FOCUSDRAWABLE, R.styleable.AlphabetView_focusDrawable);
		RE.put(RE.ATTR_FOCUSTEXTCOLOR, R.styleable.AlphabetView_focusTextColor);
		RE.put(RE.ATTR_NORMALTEXTCOLOR, R.styleable.AlphabetView_normalTextColor);
		RE.put(RE.ATTR_TABTEXTCOLOR, R.styleable.AlphabetView_tabTextColor);
		RE.put(RE.ATTR_AFONTSIZE, R.styleable.AlphabetView_aFontSize);
		
		RE.setStyleableLyric(R.styleable.lyric);
		RE.put(RE.ATTR_OFFSET_Y, R.styleable.lyric_offset_y);
		RE.put(RE.ATTR_PADDING_Y, R.styleable.lyric_padding_y);
		RE.put(RE.ATTR_HIGHLIGHTCOLOR, R.styleable.lyric_highlightColor);
		RE.put(RE.ATTR_NORMALCOLOR, R.styleable.lyric_normalColor);
		RE.put(RE.ATTR_FONTSIZE, R.styleable.lyric_fontSize);
		RE.put(RE.ATTR_DEFAULTSTRING, R.styleable.lyric_defaultString);
		RE.put(RE.ATTR_NUMROWS, R.styleable.lyric_numRows);
		RE.put(RE.ATTR_DIRECTIONS, R.styleable.lyric_directions);
		RE.put(RE.ATTR_TOUCHMODE, R.styleable.lyric_touchMode);
		RE.put(RE.ATTR_SCROLLMODE, R.styleable.lyric_scrollMode);
		RE.put(RE.ATTR_SHADOWABLE, R.styleable.lyric_shadowable);
		RE.put(RE.ATTR_SHADOWHEIGHT, R.styleable.lyric_shadowHeight);
		RE.put(RE.ATTR_SHADOWFRONTCOLOR, R.styleable.lyric_shadowFrontColor);
		RE.put(RE.ATTR_SHADOWENDCOLOR, R.styleable.lyric_shadowEndColor);
		RE.put(RE.ATTR_TOUCHLINEHEIGHT, R.styleable.lyric_touchLineHeight);
		RE.put(RE.ATTR_TOUCHLINECOLOR, R.styleable.lyric_touchLineColor);
		
		
	}
	
}