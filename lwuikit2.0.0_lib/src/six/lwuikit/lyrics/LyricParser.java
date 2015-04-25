package six.lwuikit.lyrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LyricParser {

	public LyricParser() {
		
	}
	public void parseAndStack(String identifier,String content) {
		LyricEntity entity = parse(content);
		if (null != entity && null != identifier) {
			LyricRepository.getInstance().addLyric(identifier, entity);
		}
	}
	public LyricEntity parse(String content) {
		if(null == content || content.length() <= 0) {
			return null;
		}
		LyricEntity entity = new LyricEntity();
		entity.setVersion(getValue(content, "ver:"));
		entity.setTitle(getValue(content, "ti:"));
		entity.setArtist(getValue(content, "ar:"));
		entity.setAlbum(getValue(content, "al:"));
		entity.setByEditor(getValue(content, "by:"));
		entity.setOffset(getValue(content, "offset:"));
		StringReader reader = new StringReader(content);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
		try {
			Pattern pattern = Pattern.compile("((\\[\\d{2,}\\:\\d{2,}\\.\\d{2,}\\]\\s*)+)(.*)");
			while(null != (line = br.readLine())){
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					String times = matcher.group(1);
					String str = matcher.group(3);
					List<String> list = getTimeList(times);
					for(String time:list){
						entity.addRowEntity(new RowEntity(time, str));
					}
					break;
				}
			}
			entity.sortRows();
		} catch (Exception e) {
			e.printStackTrace();
			entity.destroy();
			entity = null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			reader.close();
		}
		return entity;
	}
	private String getValue(String content, String prex){
		Pattern pattern = Pattern.compile("\\[" + prex + "(.*?)\\]");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	private List<String> getTimeList(String time) {
		Pattern pattern = Pattern.compile("\\[(\\d{2,}\\:\\d{2,}\\.\\d{2,})\\]");
		Matcher matcher = pattern.matcher(time);
		List<String> timslist = new ArrayList<String>();
		while(matcher.find()){
			String t = matcher.group(1);
			timslist.add(t);
		}
		return timslist;
	}
}
