package main.java.algorithm;
import java.util.*;
import java.util.Map;

/**
 * Implementation of the abstract class, an algorithm that takes out a random page
 * @param <K> keys for pages
 * @param <V> value for pages
 */
public class Random <K,V> extends AbstractAlgoCache <K,V>{

    //random constructor
    public Random(int capacity) {
        super(capacity);
    }

    @Override
    public V putElement(K key, V value) {
        if(getCache().containsKey(key)) {
            super.uptadeElement(key, value);
            return null;
        }
        if(this.getCache().size() < this.getCapacity()){
            Map<K,V> newElements = this.getCache();
            newElements.put(key,value);
            this.setElements(newElements);
            return null;
        }
        java.util.Random random = new java.util.Random();                //random number
        List<K> keysList = new ArrayList<K>(this.getCache().keySet());
        K randomKey = keysList.get(random.nextInt(keysList.size()));    //random key from elements HashMap
        return  super.removeAndAddElements(randomKey,key,value);
    }


}
