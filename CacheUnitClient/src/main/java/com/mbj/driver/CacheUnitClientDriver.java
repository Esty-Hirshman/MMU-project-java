package main.java.com.mbj.driver;

import main.java.com.mbj.client.CacheUnitClientObserver;
import main.java.com.mbj.view.CacheUnitView;

/**
 * main class, managed with the communications between the server and the client
 */
public class CacheUnitClientDriver {

    public static void main(String[] args) {
        CacheUnitClientObserver cacheUnitClientObserver = new CacheUnitClientObserver();
        CacheUnitView view = new CacheUnitView();
        view.addPropertyChangeListener(cacheUnitClientObserver);
        view.start();
    }
}
