package main.java.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * abstract class for implementing algorithms for extracting a page from memory, implements the interface
 * @param   <K> key for pages in memory
 * @param <V>  values of pages in memory
 */
public abstract class AbstractAlgoCache  <K,V> implements IAlgoCache <K,V>{
    private int capacity;
    private Map<K,V>  elements;


    @Override
    public V getElement(K key) {
        if(!this.elements.containsKey(key))
            return null;
        return this.elements.get(key);
    }


    @Override
    public void removeElement(K key) {
        Map<K,V> removeElement = this.getCache();
        removeElement.remove(key);
        this.setElements(removeElement);

    }
    /*constructor*/
    public AbstractAlgoCache(int capacity) {
        this.capacity = capacity;
        this.elements = new HashMap<>();
    }

    /*default constructor*/
    public AbstractAlgoCache() {
        this.capacity = 0;
        this.elements = new HashMap<>();
    }

    @Override
    public Map<K, V> getCache() {
        return elements;
    }

    public void setElements(Map<K, V> elements) {
        this.elements = elements;
    }



    public int getCapacity() {
        return capacity;
    }

    /**
     * help function remove a given element by 'keyToRemove' and add another given element by 'key' and 'value'
     * @param keyToRemove
     * @param key
     * @param value
     * @return the value of remove element
     */
    public V removeAndAddElements(K keyToRemove,K key,V value){
        Map<K,V> newElements = this.elements;
        newElements.put(key,value);
        V removeValue = newElements.remove(keyToRemove);
        this.setElements(newElements);
        return removeValue;
    }

    public void uptadeElement(K key,V value){
        elements.put(key,value);
    }
}
