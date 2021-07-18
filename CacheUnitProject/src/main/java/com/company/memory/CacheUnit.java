package main.java.com.company.memory;

import main.java.algorithm.IAlgoCache;
import main.java.com.company.dm.DataModel;

import java.util.Map;

/**
 * Manages RAM according to RAM management algorithms
 * @param <T> content type of RAM elements
 */
public class CacheUnit<T> {
    private IAlgoCache<Long, DataModel<T>> algo;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
        this.algo = algo;
    }

    /**
     * get the data of the given ids from RAM
     * @param ids ids array to get data
     * @return the data from RAM
     */
    public DataModel<T>[] getDataModels(java.lang.Long[] ids) {
        DataModel<T>[] dataArray = new DataModel[ids.length];
        for (int i = 0; i < ids.length; i++) {
            dataArray[i] = algo.getElement(ids[i]);

        }
        return dataArray;
    }

    /**
     * insert the given elements to the RAM and exits according to the algorithm if there is no place in the RAM
     * @param datamodels elements to insert
     * @return the The elements extracted by the algorithm
     */
    public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
        DataModel<T>[] removeDataModel = new DataModel[datamodels.length];
        for (int i = 0; i < datamodels.length; i++) {
            removeDataModel[i] = algo.putElement(datamodels[i].getDataModelId(), datamodels[i]);
        }
        return removeDataModel;
    }


    /**
     * remove the given elements from the RAM
     * @param ids ids of elements to remove
     */
    public void removeDataModels(java.lang.Long[] ids) {
        for (Long id : ids) {
            algo.removeElement(id);
        }
    }

    /**
     *return the cache
     */
    public Map<Long,DataModel<T>> getCache(){
        return algo.getCache();
    }

}
