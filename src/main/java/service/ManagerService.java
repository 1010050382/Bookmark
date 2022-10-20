package service;

import entity.Manager;

public class ManagerService {

    private Manager manager;

    private ManagerService() {
        manager = Manager.getInstance();
    }

    private static class ManagerServiceInstance {
        private static final ManagerService INSTANCE = new ManagerService();
    }

    public static ManagerService getInstance() {
        return ManagerServiceInstance.INSTANCE;
    }



}
