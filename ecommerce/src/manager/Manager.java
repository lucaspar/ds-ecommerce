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
    private static final String ticket = "ticket";

    public Manager() throws RemoteException {}

    // Add new items to inventory. Called by supplier.
    @Override
    public void addSupplies(String prodId, int quantity) throws RemoteException {

        inventory_ts.get(new SimpleTuple(ticket));                                      // get ticket from inventory_ts

        SimpleTuple result = (SimpleTuple) inventory_ts.readIfExists(new SimpleTuple(prodId, "*"));

        if(result == null) {
            inventory_ts.put(new SimpleTuple(prodId, quantity));
        }
        else {
            result = (SimpleTuple) inventory_ts.get(new SimpleTuple(prodId, "*"));
            int qTotal = (int) result.getData().toArray()[1];
            inventory_ts.put(new SimpleTuple(prodId, qTotal + quantity));
        }

        inventory_ts.put(new SimpleTuple(ticket));                                  // return ticket to inventory_ts

        System.out.println(Integer.toString(quantity) + " x " + prodId + " added to inventory.");

    }


    // If there are available items in inventory, place the order
    // in processed tuple space. Returns true if successful.
    @Override
    public boolean placeOrder(String orderId, String prodId, int quantity) throws RemoteException {

        inventory_ts.get(new SimpleTuple(ticket));                                      // get ticket from inventory_ts

        SimpleTuple result = (SimpleTuple) inventory_ts.readIfExists(new SimpleTuple(prodId, "*"));
        if(result == null) {
            inventory_ts.put(new SimpleTuple(ticket));                                  // return ticket to inventory_ts
            return false;
        }

        Object[] match = result.getData().toArray();
        int qTotal = (int) match[1];

        if (qTotal >= quantity) {                   // items available, place order

            inventory_ts.put(new SimpleTuple(prodId, qTotal-quantity));
            processed_ts.get(new SimpleTuple(ticket));                                  // get ticket from processed_ts

            processed_ts.put(new SimpleTuple(orderId, prodId, quantity));               // processed_ts critical section
            processed_ts.put(new SimpleTuple(ticket));                                  // return ticket to processed_ts

            inventory_ts.put(new SimpleTuple(ticket));                                  // return ticket to inventory_ts

            System.out.println("Order " + orderId + " was placed and processed.");

            return true;

        }

        inventory_ts.put(new SimpleTuple(ticket));                                      // return ticket to inventory_ts

        System.out.println("Order " + orderId + " could not be processed.");

        return false;

    }


    // Returns true if order is in processed tuple space.
    @Override
    public boolean checkProcessed(String orderId) throws RemoteException {

        SimpleTuple result = (SimpleTuple) processed_ts.readIfExists(new SimpleTuple(orderId, "*", "*"));

        String connective = !(result == null) ? "" : "not ";
        System.out.println("Order " + orderId + " is " + connective + "in processed tuple space.");

        return !(result == null);

    }

    // Initializes tuple spaces with this server
    public static void initTupleSpaces() {

        if (tm == null)             { tm = new TupleSpaceManager(); }
        if (inventory_ts == null)   { inventory_ts = tm.getSpace("Inventory"); }
        if (processed_ts == null)   { processed_ts = tm.getSpace("Processed"); }

        //inventory_ts.put(new SimpleTuple("prod1", 4));
        //processed_ts.put(new SimpleTuple("ord22", "prod1", 2));

        // create single access tickets
        inventory_ts.put(new SimpleTuple(ticket));
        processed_ts.put(new SimpleTuple(ticket));

    }

}
