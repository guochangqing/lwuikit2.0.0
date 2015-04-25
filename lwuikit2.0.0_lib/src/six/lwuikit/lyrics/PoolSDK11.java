package six.lwuikit.lyrics;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
/**
 * Downward compatible system below 11 AsyncTask<br/>
 * 
 * In Android 1.6 (Donut) to 2.3.2 (Gingerbread), AsyncTask parallel execution, the execution of 5 at the same time<br/>
 * 
 * Android 3, or SDK/API 11 and later versions<br/>
 * 
 * (execute) submitted tasks, according to the order in time to run a<br/>
 * 
 * Reference resources:<font color="red">http://blog.csdn.net/hitlion2008/article/details/7983449</font>
 * 
 * @author guochangqing
 * */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PoolSDK11 {
	@SuppressWarnings("unchecked")
	public static <P> void executeOnThreadPool(AsyncTask<P, ?, ?> task, P... params) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
