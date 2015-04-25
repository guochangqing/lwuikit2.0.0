package six.lwuikit.lyrics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface LyricLogicInterface {
	void onDraw(Canvas canvas,Paint hpaint,Paint mpaint,boolean highlight);
	Rect onLayout(Rect root,Rect last,Paint hpaint,Paint mpaint);
	Rect onScroll(Rect root,Rect last);
}
