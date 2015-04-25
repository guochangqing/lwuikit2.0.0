package six.lwuikit.app.utils;

import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class LyricManager {
	//动作_加载歌词
	public static final String ACTION_LOAD_LYRIC = "action.load.lyric";
	//动作_歌词更新 
	public static final String ACTION_LYRIC_CHANGED = "action.lyric.changed";
	//参数_歌曲保存路径
	public static final String PARAMS_SONG_PATH = "params.song.path";
	//参数_歌词保存路径
	public static final String PARAMS_LYRIC_PATH = "params.lyric.path";
	//参数_歌曲名称
	public static final String PARAMS_SONG_NAME = "params.song.name";
	//参数_歌手名称
	public static final String PARAMS_ARTIST_NAME = "params.artist.name";
	//参数_专辑名称
	public static final String PARAMS_ALBUM_NAME = "params.album.name";
	//参数_数据
	public static final String PARAMS_DATA = "params.data";
	//参数_是否用获取到的歌词替换老的歌词（内存中替换）
	public static final String PARAMS_OVERLAPABLE = "params.overlapable";
	
	private Context mContext;
	private LyricManager() {}
	private static class Singleton {
		private static LyricManager mLyricManager = new LyricManager();
	}
	////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	//***
	//  外部接口
	//***********
	public static LyricManager getInstance() {
		return Singleton.mLyricManager;
	}
	public void initManager(Context context) {
		mContext = context;
	}
	public void registerReceiver() {
		if(null == mContext) {
			throw new IllegalArgumentException("registerReceiver has failed, please uses function what is named 'initLyricManager' before this");
		}
		IntentFilter f = new IntentFilter();
		f.addAction(ACTION_LOAD_LYRIC);
		mContext.registerReceiver(mReceiver, f);
	}
	public void unRegisterRecerver() {
		if(null == mContext) {
			return;
		}
		mContext.unregisterReceiver(mReceiver);
	}
	////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	//***
	//  私有接口
	//***********
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			if(NotifyController.META_CHANGED.equals(intent.getAction()) && 
//					MusicProxy.getQueueSize() <= 0) {
//				return;
//			}
			Log.e("aa", "LyricManager receive message");
			ThreadPolicy policy = ThreadPolicy.createPolicy(intent);
			synchronized(Singleton.mLyricManager) {
				if(runnables.size() > 0 && 
						runnables.containsKey(policy.identifier)) {
					Log.e("aa", "thread existed:::"+policy.identifier);
					return;
				}
				Log.e("aa", "LyricManager create policy and excute thread:::"+policy.identifier);
				runnables.put(policy.identifier, policy);
				THREAD_POOL_EXECUTOR.execute(new LyricRunnable(policy,mNotifyListener));
			}
		}
	};
	
	private NotifyListener mNotifyListener = new NotifyListener() {

		@Override
		public void onNotify(String identifier) {
			// TODO Auto-generated method stub
			synchronized(Singleton.mLyricManager) {
				ThreadPolicy policy = runnables.remove(identifier);
				if(null == policy){
					policy = new ThreadPolicy();
				}
				Intent intent = new Intent();
				intent.setAction(ACTION_LYRIC_CHANGED);
				intent.putExtra(PARAMS_SONG_PATH, ""+policy.songpath);
				intent.putExtra(PARAMS_LYRIC_PATH, ""+policy.lyricpath);
				intent.putExtra(PARAMS_SONG_NAME, ""+policy.songname);
				intent.putExtra(PARAMS_ARTIST_NAME, ""+policy.artistname);
				intent.putExtra(PARAMS_ALBUM_NAME, ""+policy.albumname);
				intent.putExtra(PARAMS_DATA, ""+policy.data);
				mContext.sendBroadcast(intent);
			}
		}
		
	};
	
	private Hashtable<String,ThreadPolicy> runnables = new Hashtable<String,ThreadPolicy>();
	
	class LyricRunnable implements Runnable {
		private ThreadPolicy mThreadPolicy;
		private NotifyListener listener;
		LyricRunnable (ThreadPolicy policy,NotifyListener l) {
			mThreadPolicy = policy;
			listener = l;
		}
		
		public ThreadPolicy getThreadPolicy() {
			return mThreadPolicy;
		}
		
		public NotifyListener getListener() {
			return listener;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.e("aa", mThreadPolicy.identifier + "______thread is running");
			try {
				Thread.sleep(new Random().nextInt(12000));
			} catch (InterruptedException e) {
			}
			sendMsg("hello");
		}
		private void sendMsg(String path) {
			mThreadPolicy.lyricpath = path;
			if(null != listener) {
				listener.onNotify(mThreadPolicy.identifier);
			}
		}
	}
	private interface NotifyListener {
		public void onNotify(String identifier);
	}
	static class ThreadPolicy {
		//是否替换内存中已经存在的歌词
		public boolean overlapable = false;
		public String songpath;
		public String lyricpath;
		public String songname;
		public String artistname;
		public String albumname;
		public String data;
		public String identifier;
		public static ThreadPolicy createPolicy(Intent intent) {
			ThreadPolicy policy = new ThreadPolicy();
			policy.overlapable = intent.getBooleanExtra(PARAMS_OVERLAPABLE, false);
			policy.songpath = intent.getStringExtra(PARAMS_SONG_PATH);
			policy.songname = intent.getStringExtra(PARAMS_SONG_NAME);
			policy.artistname = intent.getStringExtra(PARAMS_ARTIST_NAME);
			policy.albumname = intent.getStringExtra(PARAMS_ALBUM_NAME);
			policy.data = intent.getStringExtra(PARAMS_DATA);
			policy.identifier = ""+policy.songname+"-"+policy.artistname;
			return policy;
		}
	}
	
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
}
