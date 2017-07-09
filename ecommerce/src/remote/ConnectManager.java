package remote;

import manager.ManagerInterface;

import java.rmi.Naming;

public class ConnectManager {

    private static final String managerAddress = "rmi://localhost/Manager";

    public static ManagerInterface connect2Manager(String managerIP) {

        if (managerIP == null) {
            managerIP = managerAddress;
        } else {
            managerIP = "rmi://" + managerIP + "/Manager";
        }

        try {

            ManagerInterface mInt = (ManagerInterface) Naming.lookup(managerIP);
            System.out.println("\t:: Connected to Manager ::\n");
            return mInt;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
