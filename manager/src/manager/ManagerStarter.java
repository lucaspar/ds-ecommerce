package manager;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

class ManagerStarter {

    public static void main(String[] args) {

        System.out.println("\t\t======================");
        System.out.println("\t\t  TupleSpace Manager  ");
        System.out.println("\t\t======================\n");

        //System.setSecurityManager(new SecurityManager());

        try {

            // setting RMI object
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/Manager", new Manager());
            System.out.println("Created RMI server object");

        }
        catch (Exception e) {
            System.out.println("Could not create object.");
            e.printStackTrace();
        }

        // create tuple spaces
        Manager.initTupleSpaces();

    }

}
