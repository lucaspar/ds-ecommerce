package manager;

import js.co.uk.tuplespace.space.Space;
import js.co.uk.tuplespace.space.TupleSpaceManager;
import js.co.uk.tuplespace.tuple.SimpleTuple;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Manager extends UnicastRemoteObject implements ManagerInterface {

    private static Space inventory_ts;
    private static Space processed_ts;
    private static TupleSpaceManager tm;

    public Manager() throws RemoteException {}

    @Override
    public void addSupplies(String prodId, int quantity) throws RemoteException {
        inventory_ts.put(new SimpleTuple(prodId, quantity));

        System.out.println(inventory_ts.listAllTuples());
    }

    @Override
    public boolean placeOrder(String orderId, String prodId, int quantity) throws RemoteException {
        processed_ts = tm.getSpace("Processed");
        processed_ts.put(new SimpleTuple("order00321", "prod00123", 3));

        System.out.println(processed_ts.listAllTuples());

        return true;
    }

    @Override
    public boolean checkProcessed(String orderId) throws RemoteException {

        Object allProcessed = processed_ts.listAllTuples();
        allProcessed.toString();

        System.out.println(allProcessed);

        //return allProcessed.contains(orderId);
        return true;

    }

    public static void createTS() {

        if (tm == null)             { tm = new TupleSpaceManager(); }
        if (inventory_ts == null)   { inventory_ts = tm.getSpace("Inventory"); }
        if (processed_ts == null)   { processed_ts = tm.getSpace("Processed"); }

    }

}
