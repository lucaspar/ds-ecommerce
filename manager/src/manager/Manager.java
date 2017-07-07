package manager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Manager extends UnicastRemoteObject implements ManagerInterface {

    public Manager() throws RemoteException {}

    @Override
    public void addSupplies(String test) throws RemoteException {
        System.out.println("Adding supplies to inventory: " + test);
    }

    @Override
    public boolean placeOrder(String test) throws RemoteException {
        System.out.println("Placing order: " + test);
        return true;
    }

    @Override
    public boolean checkProcessed(String test) throws RemoteException {
        System.out.println("Checking processed: " + test);
        return true;
    }

}
