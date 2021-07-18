package main.java.com.company.services;

import main.java.algorithm.LRUAlgoCacheImpl;
import main.java.com.company.dao.DaoFileImpl;
import main.java.com.company.dao.IDao;
import main.java.com.company.dm.DataModel;
import main.java.com.company.memory.CacheUnit;

import java.io.IOException;
import java.util.Map;

/**
 * Receives the content of the request from the customer
 * and works with it in front of the cacheUnit and Idao
 * @param <T>
 */
public class CacheUnitService<T> {
    private static final int CAPACITY = 2;
    private static final String ALGORITHM = "LRU";
    private static int NUM_REQUESTS = 0;
    private static  int NUM_DATA_MODELS = 0;
    private static  int NUM_SWAPS = 0;

    private IDao<Long, DataModel<T>> dao;
    private CacheUnit<T> cacheUnit;

    /**
     * constructor
     */
    public CacheUnitService() {
        dao = new DaoFileImpl<>(".\\src\\main\\resources\\datasource.json");
        cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(CAPACITY));
    }

    /**
     * Returns the requested elements which CACHE
     * if an element is not there puts it in CACHE and what comes out saves in the dao
     * @param dataModels to get
     * @return The requested data
     */
    public DataModel<T>[] get(DataModel<T>[] dataModels) {
        NUM_REQUESTS += 1;
        NUM_DATA_MODELS += dataModels.length;
        DataModel<T>[] dataToReturn = new DataModel[dataModels.length];
        DataModel<T> dataToSave;
        for (int i = 0; i < dataModels.length; i++) {
            dataToReturn[i] = cacheUnit.getDataModels(new Long[]{dataModels[i].getDataModelId()})[0];
            //if not in cache
            if (dataToReturn[i] == null) {
                //search in dao
                dataToReturn[i] = dao.find(dataModels[i].getDataModelId());
                if (dataToReturn[i] == null) {
                    dataToReturn[i]=dataModels[i];
                }
                //save in cache
                synchronized (CacheUnit.class){
                    dataToSave = cacheUnit.putDataModels(new DataModel[]{dataModels[i]})[0];
                }
                //if data came out save in dao
                if(dataToSave!=null) {
                    try {
                        NUM_SWAPS += 1;
                        dao.save(dataToSave);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dataToReturn;
    }

    /**
     *Updates the information in CACHE if it is not there and enters it
     * @param dataModels to update
     * @return true if success else - false
     */
    public Boolean update(DataModel<T>[] dataModels) {
        NUM_REQUESTS += 1;
        NUM_DATA_MODELS += dataModels.length;
        Long ids[] =  getIds(dataModels);
        DataModel<T>[] removeItems;
        synchronized (CacheUnit.class){
            removeItems = cacheUnit.putDataModels(dataModels);
        }
        for(DataModel<T> elem : removeItems){
            if(elem != null){
                try {
                    synchronized (DaoFileImpl.class){
                        NUM_SWAPS += 1;
                        dao.save(elem);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * delete data from cache and from dao
     * @param dataModels
     * @return
     */
    public  Boolean delete(DataModel<T>[] dataModels) {
        NUM_REQUESTS += 1;
        NUM_DATA_MODELS += dataModels.length;
        Long ids[] =  getIds(dataModels);
        synchronized (CacheUnit.class){
            cacheUnit.removeDataModels (ids);
        }
        for(DataModel<T> entity:dataModels){
            try {
                synchronized (DaoFileImpl.class){
                    dao.delete(entity);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Auxiliary function to get only data ids
     * @param dataModels ids to get
     * @return ids in array
     */
    private Long[] getIds(DataModel<T>[] dataModels){
        Long ids[] = new Long[dataModels.length];

        for (int i =0 ;i<dataModels.length;i++) {
            ids[i] = dataModels[i].getDataModelId();

        }
        return ids;
    }

    /**
     * When the server goes down copies all the information from CACHE to DAO
     */
    public void stop() {
        Map<Long,DataModel<T>> cache = cacheUnit.getCache();
        for (Map.Entry<Long,DataModel<T>> entry : cache.entrySet()){
            try {
                synchronized (IDao.class){
                    dao.save(entry.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * create a string with server statistics, the capacity, algorithm ype, number of requests to server,
     * number of detaModels in all requests, and number of swaps from RAM to hard disk in all requests.
     * @return statistics to client and show it in UI
     */
    public String getStatistics() {
        String stats = "Capacity: " + CAPACITY +"\nAlgorithm: " + ALGORITHM + "\nTotal number of request: " + NUM_REQUESTS +"\nTotal number of dataModels: " +
                NUM_DATA_MODELS + "\nTotal number of dataModel swaps: " +NUM_SWAPS;
        return stats;

    }
}
