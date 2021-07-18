package main.java.com.company.dm;
import java.io.Serializable;
import  java.lang.Long;
import java.util.Objects;

/**
 *Holds the information of the memory and its functions
 * @param <T> content type
 */
public class DataModel <T> implements Serializable {
    private  Long id;
    private T content;

    public DataModel(Long id, T content) {
        this.id = id;
        this.content = content;
    }

    public Long getDataModelId() {
        return id;
    }

    public void setDataModelId(Long id) {
        this.id = id;
    }

    public T getContent() {
        return content;
    }


    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return Objects.equals(content, dataModel.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}
