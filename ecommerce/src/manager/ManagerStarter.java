package manager;

import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Enumeration;
import java.util.List;

class ManagerStarter {

    public static void main(String[] args) {

        System.out.println("\t\t======================");
        System.out.println("\t\t  TupleSpace Manager  ");
        System.out.println("\t\t======================\n");

        //System.setSecurityManager(new SecurityManager());
        String rmiAddr;

        try {                                                           // try rebind with local IP

            String localIP = getLocalIP();
            rmiAddr = "rmi://" + localIP + "/Manager";

            // setting RMI object
            LocateRegistry.createRegistry(1099);
            Naming.rebind(rmiAddr, new Manager());
            System.out.println("\tCreated RMI server object\n\t" + rmiAddr);

        }
        catch (Exception e) {
            try {                                                       // try rebind with localhost

                rmiAddr = "rmi://localhost/Manager";
                Naming.rebind(rmiAddr, new Manager());
                System.out.println("\tCreated RMI server object\n\t" + rmiAddr);

            } catch (Exception e1) {
                e.printStackTrace();
                e1.printStackTrace();
            }

        }

        // create tuple spaces
        Manager.initTupleSpaces();

    }

    private static String getLocalIP() {

        Enumeration<NetworkInterface> nets;

        try {

            nets = NetworkInterface.getNetworkInterfaces();
            String strAddr = null;

            while (nets != null && nets.hasMoreElements()) {
                NetworkInterface net = nets.nextElement();
                List<InterfaceAddress> addresses = net.getInterfaceAddresses();
                for (InterfaceAddress addr : addresses) {
                    strAddr = addr.getAddress().toString();
                    if (strAddr.contains("192.168") || strAddr.contains("10.")) {
                        strAddr = strAddr.replace("/", "");
                        return strAddr;
                    }
                }
            }

            return strAddr;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
