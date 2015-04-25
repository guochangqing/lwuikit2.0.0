package six.lwuikit.lyrics;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.mozilla.universalchardet.CharsetDetectUtil;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

public class LyricFetcher {

	public LyricFetcher() {
	}
	
	public String synFetchLyricByFile(File file) {
		if(null == file || !file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			String charset = CharsetDetectUtil.getCharsetFromStream(fis);
			fis.close();
			if(null == charset) {
				FileInputStream fis2 = new FileInputStream(file);
				charset = isUtf8Charset(fis2)?"utf-8":"gbk";
				fis2.close();
			}
			FileInputStream fis3 = new FileInputStream(file);
			String content = synFetchLyricByStream(file.getAbsolutePath(),fis3,charset);
			fis3.close();
			return content;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String synFetchLyricByStream(String path,InputStream is,String charset) {
		if(null == is){
			return null;
		}
		try {
			InputStreamReader isr = new InputStreamReader(is,charset);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuffer result = new StringBuffer();
			while ((line = br.readLine()) != null) {
				if (line.trim().equals(""))
					continue;
				result.append(line);
				result.append("\r\n");
			}
			br.close();
			isr.close();
			if(result.length()>0){
				return result.toString();
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public String synFetchLyricByFilePath(String path) {
		if(null == path){
			return null;
		}
		return synFetchLyricByFile(new File(path));
	}
	
	public String synFetchLyricByUri(String uri) {
		if(null == uri) {
			return null;
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();
			conn.setRequestProperty("Accept-Charset", "gbk");  
			conn.setUseCaches(false);
	        conn.setConnectTimeout(5 * 1000);  
	        conn.setRequestMethod("GET"); 
	        conn.connect();
			InputStream is = new BufferedInputStream(conn.getInputStream());
			String content = synFetchLyricByStream(null,is,"gbk");
			is.close();
			conn.disconnect();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void asynFetchLyricByUri(String uri,LyricFetchInterface lbi) {
		if(null == uri) {
			if(null != lbi){
				lbi.onFetchFail(uri);
			}
			return;
		}
		LyricTask task = new LyricTask(uri,lbi);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            PoolSDK11.executeOnThreadPool(task);
        } else {
        	task.execute();
        }
	}
	private boolean isUtf8Charset(InputStream stream){
		boolean utf8 = false;
		try {
			String line = null;
			InputStreamReader isr = new InputStreamReader(stream, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (!TextUtils.isEmpty(line.trim())) {
					break;
				}
			}
			if(null == line){
				utf8 = true;
			}else{
				if(line.equals(new String(line.getBytes("gbk"), "gbk"))){
					utf8 = true;
				}
			}
			br.close();
			isr.close();
		} catch (Exception e) {
		}
		return utf8;
	}
	class LyricTask extends AsyncTask<Void, Void, String>{
		private LyricFetchInterface lbi;
		private String url;
		LyricTask(String uri,LyricFetchInterface l){
			lbi = l;
			url = uri;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(null != lbi){
				lbi.onFetchStart(url);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(null == result){
				if(null != lbi){
					lbi.onFetchFail(url);
				}
			}else{
				if(null != lbi){
					lbi.onFetchSuccess(url, result);
				}
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			if(null != lbi){
				lbi.onFetchFail(url);
			}
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return synFetchLyricByUri(url);
		}
	};
}
