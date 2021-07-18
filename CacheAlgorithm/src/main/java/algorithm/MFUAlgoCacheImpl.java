package main.java.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the abstract class,an algorithm that takes out the page that has been used the most times recently
 * @param <K> keys for pages
 * @param <V> value for pages
 */
public class MFUAlgoCacheImpl <K,V> extends AbstractAlgoCache <K,V>{

    private Map<K,Integer> counters;


    public MFUAlgoCacheImpl(int capacity) {
        super(capacity);
        this.counters = new HashMap<>();
    }

    @Override
    public V getElement(K key) {
        V getValue = super.getElement(key);
        if(getValue != null) {
            Integer currentCounter = counters.get(key);
            counters.put(key, currentCounter + 1);
        }
        return getValue;
    }

    @Override
    public V putElement(K key, V value) {
        if(getCache().containsKey(key)) {
            super.uptadeElement(key, value);
            counters.put(key,counters.get(key)+1);
            return null;
        }
        if(getCache().size() < getCapacity()){ //if there is pace in ram
            Map <K,V> newElement = getCache();
            newElement.put(key,value);
            counters.put(key,0);
            setElements(newElement);
            return null;
        }
        K keyToRemove = this.counters.entrySet().stream().max((entry1,entry2)->entry1.getValue() > entry2.getValue()?1:-1)
                .get().getKey();   //get highest counter to remove
        counters.put(key,0);
        counters.remove(keyToRemove);
        return  super.removeAndAddElements(keyToRemove,key,value);
    }

    @Override
    public void removeElement(K key) {
        if(this.getCache().containsKey(key)){
            super.removeElement(key);
            counters.remove(key);

        }
    }

}
