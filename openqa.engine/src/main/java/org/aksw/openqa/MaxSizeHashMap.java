package org.aksw.openqa;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2266588887193180370L;
	private int maxSize;

    public MaxSizeHashMap(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

	public void setCapacity(int capacity) {
		this.maxSize = capacity;
	}
}