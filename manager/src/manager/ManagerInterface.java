package manager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ManagerInterface extends Remote {

    void addSupplies(String prodId, int quantity) throws RemoteException;
    boolean placeOrder(String orderId, String prodId, int quantity) throws RemoteException;
    boolean checkProcessed(String orderId) throws RemoteException;

}
