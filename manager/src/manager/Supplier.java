package manager;

import java.rmi.Naming;
import java.util.Random;

class Supplier {

    public static void main (String[] args) {

        System.out.println("\t\t============");
        System.out.println("\t\t| SUPPLIER |");
        System.out.println("\t\t============\n");

        ManagerInterface warehouse;
        String name = "rmi://localhost/Manager";

        try {

            warehouse = (ManagerInterface) Naming.lookup(name);
            Random rdm = new Random();

            while(true) {

                String randomProdId = "prod" + String.format("%05d", rdm.nextInt(9999));
                int randomQuantity = rdm.nextInt(9)+1;

                warehouse.addSupplies(randomProdId, randomQuantity);
                System.out.println(randomProdId + ", " + Integer.toString(randomQuantity));

                Thread.sleep(10000);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}