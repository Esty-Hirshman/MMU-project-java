package main.test.Algorithm;
import main.java.algorithm.AbstractAlgoCache;
import main.java.algorithm.LRUAlgoCacheImpl;
import main.java.algorithm.MFUAlgoCacheImpl;
import main.java.algorithm.Random;
import org.junit.Assert;
import org.junit.Test;

public class IAlgoCacheTest {

    private AbstractAlgoCache RAM;

    @Test
    public void MFUAlgoTest(){
        RAM = new MFUAlgoCacheImpl<Integer,String>(3);
        RAM.putElement(2,"esty");
        RAM.putElement(3,"aaaa");
        RAM.putElement(5,"bbbb");
        RAM.getElement(2);
        RAM.getElement(3);
        RAM.getElement(3);
        Assert.assertEquals(RAM.putElement(1,"dddd"),"aaaa");
        Assert.assertEquals(RAM.getElement(2),"esty");
        Assert.assertEquals(RAM.getElement(5),"bbbb");

    }

    @Test
    public void LRUAlgoTest(){
        RAM = new LRUAlgoCacheImpl<Integer,String>(4);
        RAM.putElement(1,"cccc");
        RAM.putElement(3,"aaaa");
        RAM.putElement(2,"bbbb");
        RAM.getElement(2);
        Assert.assertEquals(RAM.putElement(6,"tttt"),null);
        Assert.assertEquals(RAM.putElement(7,"hhhh"),"cccc");
        Assert.assertEquals(RAM.getElement(3),"aaaa");
        Assert.assertEquals(RAM.putElement(8,"eeee"),"bbbb");
        Assert.assertEquals(RAM.putElement(9,"mmmm"),"tttt");


    }

    @Test
    public void RandomAlgoTest(){
        RAM = new Random(3);
        Assert.assertEquals(RAM.putElement(1,"aaa"),null);
        Assert.assertEquals(RAM.putElement(2,"bbb"),null);
        Assert.assertEquals(RAM.putElement(3,"ccc"),null);
        Assert.assertEquals(RAM.getElement(1),"aaa");
        Assert.assertEquals(RAM.getElement(2),"bbb");
        Assert.assertEquals(RAM.getElement(3),"ccc");
        System.out.println(RAM.putElement(4,"ddd"));
    }
}

