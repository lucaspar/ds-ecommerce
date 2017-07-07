import js.co.uk.tuplespace.space.Space;
import js.co.uk.tuplespace.space.TupleSpaceManager;
import js.co.uk.tuplespace.tuple.SimpleTuple;

public class Main {

    private static Space inventory_ts;
    private static Space processed_ts;

    public static void main(String[] args) {

        System.out.println("\t\t=============================");
        System.out.println("\t\t| Initializing Tuple Spaces |");
        System.out.println("\t\t=============================");

        createTS();

    }

    private static void createTS() {

        TupleSpaceManager tm = new TupleSpaceManager();
        inventory_ts = tm.getSpace("Inventory");
        processed_ts = tm.getSpace("Processed");

        inventory_ts.put(new SimpleTuple("prod00123", 5));
        processed_ts.put(new SimpleTuple("order00321", "prod00123", 3));

        System.out.println(inventory_ts.listAllTuples());
        System.out.println(processed_ts.listAllTuples());

        try {
            Thread.sleep(4567890);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
