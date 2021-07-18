package main.test.com.company.memory;

import main.java.algorithm.IAlgoCache;
import main.java.algorithm.LRUAlgoCacheImpl;
import main.java.com.company.dao.DaoFileImpl;
import main.java.com.company.dm.DataModel;
import main.java.com.company.memory.CacheUnit;
import org.junit.Assert;
import org.junit.Test;

public class CacheUnitTest {
    @Test
    public void cacheUnitTest(){
        IAlgoCache<Long,DataModel<String>> algo = new LRUAlgoCacheImpl<>(3);
        CacheUnit<String> cache = new CacheUnit<String>(algo);
        DataModel<String>[] dataArray = new DataModel[5];
        dataArray[0] = new DataModel<String>(1L,"1");
        dataArray[1] = new DataModel<String>(2L,"2");
        dataArray[2] = new DataModel<String>(3L,"3");
        dataArray[3] = new DataModel<String>(4L,"4");
        dataArray[4] = new DataModel<String>(1L,"5");
        Assert.assertEquals(cache.putDataModels(dataArray)[3],dataArray[0]);
        Long ids[] = new Long[2];
        ids[0] = 3L;
        ids[1] =4L;
        Assert.assertEquals(cache.getDataModels(ids)[0],dataArray[2]);
        cache.removeDataModels(ids);
        Assert.assertEquals(cache.getDataModels(ids)[0],null);
        DaoFileImpl<String> dao = new DaoFileImpl<String>(5,".\\src\\main\\resources\\datasource.json");
        dao.save(dataArray[0]);
        dao.save(dataArray[1]);
        dao.save(dataArray[2]);
        dao.save(dataArray[3]);
        dao.save(dataArray[4]);
        dao.delete(dataArray[1]);


    }
}
