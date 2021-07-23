package exercise4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentArrayList<T> {
    //this will be used to lock for writing operations
    private final Lock readLock;

    //use this to lock for read operations
    private final Lock writeLock;

    //the underlying list
    private final List<T> list = new ArrayList();

    {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    public void add(T e){
        writeLock.lock();
        try{
            list.add(e);
        }finally{
            writeLock.unlock();
        }
    }

    public T get(int index){
        readLock.lock();
        try{
            return list.get(index);
        }finally{
            readLock.unlock();
        }
    }

    public int getSize(){
        readLock.lock();
        try{
            return list.size();
        }
        finally {
            readLock.unlock();
        }
    }

    public Iterator<T> iterator(){
        readLock.lock();
        try{
            //we iterate over a snapshot of our list
            return new ArrayList<T>(list).iterator();
        }
        finally{
            readLock.unlock();
        }
    }


}
