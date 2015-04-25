package six.lwuikit.lyrics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.TextUtils;

public class RowEntity extends EntityBase implements Comparable<RowEntity>{
	/** begin time of this lrc row */
    private long time;
    /** content of this lrc */
    private String content;
    
    private String strTime;
    
    private List<String> sentences;
    
    private float mPaddingY;
    
    public float getPaddingY() {
		return mPaddingY;
	}
	public void setPaddingY(float mPaddingY) {
		this.mPaddingY = mPaddingY;
	}
    
    public RowEntity(String strTime,String content){
    	sentences = new ArrayList<String>();
        this.strTime = strTime;
        this.content = content;
        time = timeConvert(strTime);
    }
    public String obtainRowStr() {
    	if(TextUtils.isEmpty(strTime)) {
    		return null;
    	}
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("["+strTime+"]");
		if(!TextUtils.isEmpty(content)){
			buffer.append(content);
		}
		buffer.append("\r\n");
		return buffer.toString();
    }
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}
	public List<String> getSentences() {
		return sentences;
	}
	/**
	 * 文字实际矩形
	 * */
	public Rect getActualSelfRect(Paint p) {
		if(null == sentences || sentences.size() <= 0){
			return selfrect;
		}
		int vh = selfrect.height() / sentences.size();
		int dis = (int)(vh - p.getTextSize())/2;
		return new Rect(selfrect.left,selfrect.top+dis,selfrect.right,selfrect.bottom-dis);
	}
	public int sizeOfRow() {
		return null == sentences ? 0 : sentences.size();
	}
	private static long timeConvert(String timeString){
        timeString = timeString.replace('.', ':');
        String[] times = timeString.split(":");
        // mm:ss:SS
        return Integer.valueOf(times[0]) * 60 * 1000 +
                Integer.valueOf(times[1]) * 1000 +
                Integer.valueOf(times[2]) ;
    }
	@Override
	public void onDraw(Canvas canvas, Paint hpaint, Paint mpaint,boolean highlight) {
		// TODO Auto-generated method stub
		if(null == sentences || sentences.size() <= 0){
			return;
		}
		FontMetricsInt fontMetrics = mpaint.getFontMetricsInt();
		int vh = selfrect.height() / sentences.size();
		for(int i=0;i<sentences.size();i++){
			String text = sentences.get(i);
			if(TextUtils.isEmpty(text)){
				continue;
			}
			if(selfrect.top+vh*(i-1) > rootrect.bottom || 
					selfrect.top+vh*(i+1) < rootrect.top){
				continue;
			}
			int y = selfrect.top+vh*i + (vh - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top; 
			if(highlight){
				canvas.drawText(text, selfrect.width()/2, y, hpaint);
			}else{
				canvas.drawText(text, selfrect.width()/2, y, mpaint);
			}
		}
//		int color = hpaint.getColor();
//		hpaint.setColor(Color.YELLOW);
//		canvas.drawLine(selfrect.left, selfrect.top,selfrect.right , selfrect.top, hpaint);
//		hpaint.setColor(Color.RED);
//		canvas.drawLine(selfrect.left, selfrect.bottom,selfrect.right , selfrect.bottom, hpaint);
//		hpaint.setColor(color);
	}

	@Override
	public Rect onLayout(Rect root,Rect last,Paint hpaint,Paint mpaint) {
		// TODO Auto-generated method stub
		setRootRect(root);
		if(null == sentences){
			sentences = new ArrayList<String>();
		}
		sentences.clear();
		if(null == content || content.length() <= 0){
			sentences.add("");
		}else{
			String line = content;
        	while(true){
        		int num = getLineBreakNum(line,root.width(),mpaint);
        		if(num <= 0 ){
        			break;
        		}
        		if(num < line.length()){
        			sentences.add(line.substring(0, num-1));
        			line = line.substring(num-1);
        		}else{
        			sentences.add(line);
        			line = "";
        		}
        	}
		}
		float textsize = mpaint.getTextSize();
		setSelfRect(new Rect(root.left, last.bottom, root.right,
				(int) (last.bottom + (textsize + mPaddingY) * sentences.size())));
		return getSelfRect();
	}
	@Override
	public Rect onScroll(Rect root, Rect last) {
		// TODO Auto-generated method stub
		Rect r = getSelfRect();
		int height = r.height();
		r.top = last.bottom;
		r.bottom = r.top + height;
		return getSelfRect();
	}

	@Override
	public int compareTo(RowEntity another) {
		// TODO Auto-generated method stub
		return (int)(this.time - another.time);
	}
	
	private static int getLineBreakNum(String line,int viewWidth,Paint paint){
		if(TextUtils.isEmpty(line)){
			return 0;
		}
		int num = paint.breakText(line, true, viewWidth, null);
		if(line != null && num >= line.length()){
			return line.length();
		}else{
			if (num > 0) {
				for(int i=num-1;i>0;i--){
					char ch = line.charAt(i);
					if(!isLetter(ch)){
						return i+1;
					}
				}
				return num;
			}
		}
		return 0;
    }
    private static boolean isLetter(char ch){
    	return Pattern.matches("[a-zA-Z\\-]", ch+"");
    }
}
