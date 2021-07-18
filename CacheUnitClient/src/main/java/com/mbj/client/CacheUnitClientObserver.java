package main.java.com.mbj.client;

import main.java.com.mbj.view.CacheUnitView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Updated the request forwarded to the server by the client,
 * and send the client the server response
 */
public class CacheUnitClientObserver implements PropertyChangeListener {
    private CacheUnitClient client;
    private CacheUnitView updateUIClient;

    public CacheUnitClientObserver() {
        this.client = new CacheUnitClient();
    }

    /**
     * get event in property change listener,
     * and update the UI data with the server's response to the client's request
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String response = null;
        updateUIClient = (CacheUnitView) evt.getSource();
        response = client.send(evt.getNewValue().toString());
        updateUIClient.updateUIData(response);
    }
}
