package main.java.algorithm;


import java.util.Map;

/**
 * intreface for implementing algorithms for extracting a page from memory
 * @param <K>
 * @param <V>
 */
public interface IAlgoCache <K,V>{
    /**
     * get the value of element with given key
     * @param key
     * @return
     */
    public V getElement(K key);

    /**
     * insert a given element,If there is no place to insert a page puller according to the algorithm
     * @param key
     * @param value
     * @return the value of element that was remove
     */
    public V putElement(K key,V value);

    /**
     * remove the page with the given key
     * @param key of page to remove
     */
    public void removeElement(K key);

    /**
     * return the cache
     */
    public Map<K,V> getCache();


}
