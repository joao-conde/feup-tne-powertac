package org.powertac.samplebroker.repos;


public interface IRepo<K, V> {
    
    public void save(K key, V value);

    public V findById(K key);
}