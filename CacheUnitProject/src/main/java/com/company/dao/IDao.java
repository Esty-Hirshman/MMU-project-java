package main.java.com.company.dao;

import java.io.IOException;
import java.io.Serializable;

/**
 * interface that manages main memory, insertion, deletion and bargain
 * @param <ID> id type of element in memory
 * @param <T> value type of element in memory
 */
public interface IDao <ID extends Serializable,T>{

    /**
     * Keeps a new entity in the main memory
     * @param entity new entity to save
     * @throws IOException throw exception when i/o failed
     */
    public void save(T entity) throws IOException;


    /**
     * delete an entity from the main memory
     * @param entity entity to delete
     * @throws IOException throw exception when i/o failed
     */
    public void delete(T entity) throws IOException;

    /**
     * find the data of the given id from the main memory
     * @param id id to find
     * @return the data from main memory
     */
    public T find(ID id);
}
