package manager;

import java.rmi.Naming;

class Supplier {

    public static void main (String[] args) {
        ManagerInterface remoteTask;
        String name = "rmi://localhost/Manager";

        try {

            remoteTask = (ManagerInterface) Naming.lookup(name);
            remoteTask.addSupplies("prod00234, 5");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}