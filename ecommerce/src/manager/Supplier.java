package manager;

import com.amazonaws.services.sqs.AmazonSQS;

import java.rmi.RemoteException;
import java.util.Random;

import static remote.AmazonSQS.connect2Amazon;
import static remote.ConnectManager.connect2Manager;

class Supplier {

    private static AmazonSQS sqs;
    private static ManagerInterface warehouse;
    private static String pending;

    public static void main (String[] args) {

        System.out.println("\t\t============");
        System.out.println("\t\t  Supplier  ");
        System.out.println("\t\t============\n");

        sqs         = connect2Amazon();
        warehouse   = connect2Manager();
        pending     = sqs.listQueues().getQueueUrls().get(0);

        try {
            work();
        } catch (RemoteException e) {
            System.out.println("\tERROR: Failed to communicate with Manager");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void work() throws RemoteException, InterruptedException {

        // TODO: add supplies based on pending queue

        Random rdm = new Random();
        while (true) {

            String randomProdId = "prod" + String.format("%05d", rdm.nextInt(9999));
            int randomQuantity = rdm.nextInt(9) + 1;

            warehouse.addSupplies(randomProdId, randomQuantity);
            System.out.println(randomProdId + ", " + Integer.toString(randomQuantity));

            Thread.sleep(10000);

        }
    }

}