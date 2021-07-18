package main.java.com.company.services;

import main.java.com.company.dm.DataModel;

/**
 * create a separation layer between the CacheUnitService and the networking layer
 * call the CacheUnitService methods
 * @param <T>
 */
public class CacheUnitController<T> {
    private CacheUnitService<T> cacheService;

    public CacheUnitController() {
        cacheService = new CacheUnitService<>();
    }

    public DataModel<T>[] get(DataModel<T>[] dataModels) {
        return cacheService.get(dataModels);
    }

    public Boolean update(DataModel<T>[] dataModels) {
        return cacheService.update(dataModels);
    }

    public Boolean delete(DataModel<T>[] dataModels) {
        return cacheService.delete(dataModels);
    }

    public void stop() {
        cacheService.stop();
    }

    public String getStatistics() { return cacheService.getStatistics(); }
}
