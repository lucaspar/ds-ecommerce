package remote;

import manager.ManagerInterface;

import java.rmi.Naming;

public class ConnectManager {

    private static final String managerAddress = "rmi://localhost/Manager";

    public static ManagerInterface connect2Manager() {

        try {
            return (ManagerInterface) Naming.lookup(managerAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
