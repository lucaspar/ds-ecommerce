package manager;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ManagerInterface extends Remote {

    void addSupplies(String test) throws RemoteException;
    boolean placeOrder(String test) throws RemoteException;
    boolean checkProcessed(String test) throws RemoteException;

}
