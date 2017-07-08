package manager;

import com.amazonaws.services.sqs.AmazonSQS;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

import static remote.AmazonSQS.connect2Amazon;

class Supplier {

    private static ManagerInterface warehouse;
    private static AmazonSQS sqs;

    public static void main (String[] args) {

        System.out.println("\t\t============");
        System.out.println("\t\t  SUPPLIER  ");
        System.out.println("\t\t============\n");

        connect2Amazon();

        connect2Manager();

        try {
            work();
        } catch (RemoteException e) {
            System.out.println("\tERROR: Failed to communicate with Manager");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void work() throws RemoteException, InterruptedException {

        Random rdm = new Random();

        while (true) {

            String randomProdId = "prod" + String.format("%05d", rdm.nextInt(9999));
            int randomQuantity = rdm.nextInt(9) + 1;

            warehouse.addSupplies(randomProdId, randomQuantity);
            System.out.println(randomProdId + ", " + Integer.toString(randomQuantity));

            Thread.sleep(10000);

        }

    }

    private static void connect2Manager() {

        String name = "rmi://localhost/Manager";

        try {
            warehouse = (ManagerInterface) Naming.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}