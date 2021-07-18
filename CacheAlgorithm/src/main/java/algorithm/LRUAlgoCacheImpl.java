package main.java.algorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *Implementation of the abstract class, an algorithm that takes out the page whose use was the farthest
 * @param <K> keys for pages
 * @param <V> value for pages
 */
public class LRUAlgoCacheImpl <K,V> extends AbstractAlgoCache <K,V>{
    private Map<K, Long> dates;


    public LRUAlgoCacheImpl(int capacity) {
        super(capacity);
        this.dates = new HashMap<>();
    }


    @Override
    public V getElement(K key) {
        V getValue = super.getElement(key);
        if(getValue != null)
            dates.put(key,System.nanoTime() ); //returns the current value of the system timer, in nanoseconds.
        return getValue;
    }

    @Override
    public V putElement(K key, V value) {
        if(getCache().containsKey(key)) {
            super.uptadeElement(key, value);
            dates.put(key, System.nanoTime());
            return null;
        }
        if(getCache().size() < getCapacity()){ //if there is place in ram
            Map <K,V> newElement = getCache();
            newElement.put(key,value);
            dates.put(key,System.nanoTime()); //returns the current value of the system timer, in nanoseconds.
            setElements(newElement);
            return null;
        }
        K keyToRemove = this.dates.entrySet().stream().min((entry1,entry2)->entry1.getValue() > entry2.getValue() ? 1: -1)
                .get().getKey();        //get the earlier date from dates
        dates.put(key,System.nanoTime()); //returns the current value of the system timer, in nanoseconds.
        dates.remove(keyToRemove);
        return  super.removeAndAddElements(keyToRemove,key,value);
    }

    @Override
    public void removeElement(K key) {
        if(this.getCache().containsKey(key)){
            super.removeElement(key);
            dates.remove(key);

        }
    }

}
