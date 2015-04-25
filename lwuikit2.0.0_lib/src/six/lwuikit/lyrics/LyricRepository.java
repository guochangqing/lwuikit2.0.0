package six.lwuikit.lyrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LyricRepository {
	
	private LruHashMap pool;
	
	private LyricRepository() {}
	private static class Singleton {
		private static LyricRepository mLyricRepository = new LyricRepository();
	}
	public static LyricRepository getInstance() {
		return Singleton.mLyricRepository;
	}
	
	public void addLyric(String identifier,LyricEntity entity) {
		synchronized (this) {
			if(null == identifier || null == entity){
				return ;
			}
			if(null == pool) {
				pool = new LruHashMap(100);
			}
			pool.put(identifier, entity);
		}
	}
	
	public boolean removeLyric(String identifier) {
		synchronized (this) {
			if(null == pool || null == identifier) {
				return false;
			}
			return null == pool.remove(identifier) ? false : true;
		}
	}
	
	public boolean isContainsLyric(String identifier) {
		synchronized (this) {
			if(null == pool || null == identifier) {
				return false;
			}
			return pool.contains(identifier);
		}
	}
	
	public LyricEntity getLyricEntity(String identifier) {
		synchronized (this) {
			if(null == pool || null == identifier) {
				return null;
			}
			return pool.get(identifier);
		}
	}
	public void destroy() {
		synchronized (this) {
			if(null != pool) {
				pool.clear();
				pool = null;
			}
		}
	}
	class LruHashMap{
		private List<String> list;
		private Map<String,LyricEntity> map;
		private int max;
		public LruHashMap(int maxSize) {
			max = maxSize;
			list = new ArrayList<String>();
			map = new HashMap<String,LyricEntity>();
		}
		public void put(String key,LyricEntity value){
			synchronized (this) {
				if(list.size() >= max) {
					String temp = list.remove(0);
					map.remove(temp);
				}
				list.add(key);
				map.put(key, value);
			}
		}
		public LyricEntity get(String key){
			synchronized (this) {
				return map.get(key);
			}
		}
		public LyricEntity remove(String key){
			synchronized (this) {
				list.remove(key);
				return map.remove(key);
			}
		}
		public boolean contains(String key) {
			synchronized (this) {
				return map.containsKey(key);
			}
		}
		public void clear() {
			synchronized (this) {
				list.clear();
				map.clear();
			}
		}
	}
}
