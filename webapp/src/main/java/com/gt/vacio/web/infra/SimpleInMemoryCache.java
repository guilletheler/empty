package com.gt.vacio.web.infra;

import java.util.ArrayList;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

/**
 * 
 * @author PortalTheler
 *
 * @param <K>
 * @param <T>
 */
public class SimpleInMemoryCache<K, T> {

	private long timeToLive;
	private LRUMap<K, SimpleCacheObject> simpleCacheMap;

	protected class SimpleCacheObject {
		public long lastAccessed = System.currentTimeMillis();
		public T value;

		protected SimpleCacheObject(T value) {
			this.value = value;
		}
	}

	public SimpleInMemoryCache(long secondsToLive, final long secondsInterval, int maxItems) {
		this.timeToLive = secondsToLive * 1000;

		simpleCacheMap = new LRUMap<>(maxItems);

		if (timeToLive > 0 && secondsInterval > 0) {

			Thread t = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(secondsInterval * 1000);
						} catch (InterruptedException ex) {
						}
						cleanup();
					}
				}
			});

			t.setDaemon(true);
			t.start();
		}
	}

	public void put(K key, T value) {
		synchronized (simpleCacheMap) {
			simpleCacheMap.put(key, new SimpleCacheObject(value));
		}
	}

	public T get(K key) {
		synchronized (simpleCacheMap) {
			SimpleCacheObject c = (SimpleCacheObject) simpleCacheMap.get(key);

			if (c == null)
				return null;
			else {
				c.lastAccessed = System.currentTimeMillis();
				return c.value;
			}
		}
	}

	public void remove(K key) {
		synchronized (simpleCacheMap) {
			simpleCacheMap.remove(key);
		}
	}

	public int size() {
		synchronized (simpleCacheMap) {
			return simpleCacheMap.size();
		}
	}

	public void cleanup() {

		long now = System.currentTimeMillis();
		ArrayList<K> deleteKey = null;

		synchronized (simpleCacheMap) {
			MapIterator<K, SimpleCacheObject> itr = simpleCacheMap.mapIterator();

			deleteKey = new ArrayList<K>((simpleCacheMap.size() / 2) + 1);
			K key = null;
			SimpleCacheObject c = null;

			while (itr.hasNext()) {
				key = (K) itr.next();
				c = (SimpleCacheObject) itr.getValue();

				if (c != null && (now > (timeToLive + c.lastAccessed))) {
					deleteKey.add(key);
				}
			}
		}

		for (K key : deleteKey) {
			synchronized (simpleCacheMap) {
				simpleCacheMap.remove(key);
			}

			Thread.yield();
		}
	}
}
