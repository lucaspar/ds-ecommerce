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

    // Add new items to inventory. Called by supplier.
    @Override
    public void addSupplies(String prodId, int quantity) throws RemoteException {

        // TODO: add access control with ticket

        SimpleTuple result = (SimpleTuple) inventory_ts.readIfExists(new SimpleTuple(prodId, "*"));

        if(result == null) {
            inventory_ts.put(new SimpleTuple(prodId, quantity));
        }
        else {
            result = (SimpleTuple) inventory_ts.get(new SimpleTuple(prodId, "*"));
            int qTotal = (int) result.getData().toArray()[1];
            inventory_ts.put(new SimpleTuple(prodId, qTotal + quantity));
        }

        System.out.println(inventory_ts.listAllTuples());
    }


    // If there are available items in inventory, place the order
    // in processed tuple space. Returns true if successful.
    @Override
    public boolean placeOrder(String orderId, String prodId, int quantity) throws RemoteException {

        // TODO: add access control with ticket

        SimpleTuple result = (SimpleTuple) inventory_ts.readIfExists(new SimpleTuple(prodId, "*"));
        if(result == null) {
            return false;
        }

        Object[] match = result.getData().toArray();
        int qTotal = (int) match[1];

        if (qTotal >= quantity) {                   // items available, place order

            inventory_ts.put(new SimpleTuple(prodId, qTotal-quantity));
            processed_ts.put(new SimpleTuple(orderId, prodId, quantity));
            return true;

        }

        return false;

    }


    // Returns true if order is in processed tuple space.
    @Override
    public boolean checkProcessed(String orderId) throws RemoteException {

        SimpleTuple result = (SimpleTuple) processed_ts.readIfExists(new SimpleTuple(orderId, "*", "*"));

        return !(result == null);

    }

    // Initializes tuple spaces with this server
    public static void initTupleSpaces() {

        if (tm == null)             { tm = new TupleSpaceManager(); }
        if (inventory_ts == null)   { inventory_ts = tm.getSpace("Inventory"); }
        if (processed_ts == null)   { processed_ts = tm.getSpace("Processed"); }

        inventory_ts.put(new SimpleTuple("prod1", 4));
        SimpleTuple result = (SimpleTuple) inventory_ts.readIfExists(new SimpleTuple("prod1", "*"));

        if(result != null) {
            System.out.println(result.getData().toArray()[1]);
        }

    }

}
