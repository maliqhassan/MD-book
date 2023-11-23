package com.alsam.mdbook_01;

import java.util.ArrayList;

public class DataManager {

    private User me;
    private ArrayList<User> pushQueue;


    private static DataManager dataManager;
    private LocalStorageController localStorageController;

    /**
     * @return Singleton instance of DataManager
     */
    public static DataManager getDataManager() {
        if (dataManager == null){
            dataManager = new DataManager();
            dataManager.localStorageController = LocalStorageController.getController();
            dataManager.setPushQueue(LocalStorageController.getController().loadQueue());
        }
        return dataManager;
    }

    /**
     * Queues a changed user object up for upload
     * @param user The user to be uploaded.
     */
    public void addToQueue(User user){
        pushQueue.add(user);
        localStorageController.saveQueue(pushQueue);
    }

    /**
     * @param pushQueue The array of User objects to be uploaded.
     */
    public void setPushQueue(ArrayList<User> pushQueue) {
        this.pushQueue = pushQueue;
    }

    /**
     * @param user The user object to be removed from the queue.
     */
    public void removeFromQueue(User user){
        pushQueue.remove(user);
        localStorageController.saveQueue(pushQueue);
    }

    /**
     * @return The queue of users to be pushed.
     */
    public ArrayList<User> getPushQueue() {
        return pushQueue;
    }

    /**
     * Saves the data locally for the logged in user.
     * @param user The user to log in as.
     */
    public void saveMe(User user){
        me = user;
        localStorageController.saveMe(me);
    }

    /**
     * Clears the user data out from storage. Should be called on logout.
     */
    public void removeMe(){
        me = null;
        localStorageController.clearMe();
    }
}
