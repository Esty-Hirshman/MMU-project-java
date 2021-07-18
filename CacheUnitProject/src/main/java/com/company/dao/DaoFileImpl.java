package main.java.com.company.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.company.dm.DataModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * implements the IDao interface, Updates the file used as main memory
 * @param <T> type of data in file
 */
public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {
    private List<DataModel<T>> fileContent;
    private int capacity;
    private String filePath;

    /* constructor*/
    public DaoFileImpl(String filePath) {
        this.capacity = 1000;
        this.filePath = filePath;
        fileContent = new ArrayList<>();
    }

    /* constructor*/
    public DaoFileImpl(int capacity, String filePath) {
        this.capacity = capacity;
        this.filePath = filePath;
        fileContent = new ArrayList<>();
    }

    /**
     * override for interface, save entity in file
     * @param entity new entity to save in file
     */
    @Override
    public void save(DataModel<T> entity) {
        try {
            this.readFile();
            if (this.fileContent.size() == this.capacity) {
                System.out.println("the memory is full, can't add entity: " + entity.toString());
                return;
            }
            if(this.find(entity.getDataModelId()) != null){
                fileContent.removeIf(item -> item.getDataModelId().equals(entity.getDataModelId()));
            }
            fileContent.add(entity);
            Gson gson = new Gson();
            String entityToWrite = gson.toJson(fileContent);
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(entityToWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * override for interface, delete entity in file
     * @param entity  entity to delete in file
     */
    @Override
    public void delete(DataModel<T> entity) {
        Gson gson = new Gson();
        try {
            this.readFile();
            fileContent.removeIf(item -> item.getDataModelId().equals(entity.getDataModelId()));
            FileWriter fileWriter = new FileWriter(filePath);
            String toWrite = gson.toJson(fileContent);
            fileWriter.write(toWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * override for interface, delete entity in file
     * @param id id to find
     * @return the entity that was found
     */
    @Override
    public DataModel<T> find(Long id) {
        this.readFile();
        for (DataModel<T> entry : fileContent) {
            if (entry.getDataModelId().equals(id)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * help function to read the content file to fileContent List
     */
    private void readFile() {
        Gson gson = new Gson();
        try {
            Type listType = new TypeToken<ArrayList<DataModel<T>>>() {
            }.getType();
            FileReader fileReader = new FileReader(filePath);
            ArrayList<DataModel<T>> fileArray = gson.fromJson(fileReader, listType);
            if (fileArray != null) {
                fileContent = fileArray;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
