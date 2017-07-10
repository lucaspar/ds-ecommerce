package remote;

import manager.ManagerInterface;

import java.rmi.Naming;

public class ConnectManager {

    public static ManagerInterface connect2Manager(String managerIP) {

        int port = 1099;

        String lookupAddr;
        if (managerIP == null) {
            lookupAddr = "rmi://localhost:" + port + "/Manager";
        } else {
            lookupAddr = "rmi://" + managerIP + ":" + port + "/Manager";
        }

        try {

            ManagerInterface mInt = (ManagerInterface) Naming.lookup(lookupAddr);
            System.out.println("\t:: Connected to Manager ::\n\n\t"+lookupAddr);
            return mInt;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
