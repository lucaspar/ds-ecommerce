package manager;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

        String managerIP = null;
        if (args.length > 0) {
            managerIP = args[0];
        }

        sqs         = connect2Amazon();
        warehouse   = connect2Manager(managerIP);
        pending     = sqs.listQueues().getQueueUrls().get(0);

        try {
            work();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("\tERROR: Failed to communicate with Manager");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void work() throws RemoteException, InterruptedException {

        Random rdm = new Random();
        String orderID, productID;
        int quantity;

        while (true) {

            ReceiveMessageRequest receiveMessageRequest =
                    new ReceiveMessageRequest(pending).withMessageAttributeNames("orderID", "productID", "quantity");
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

            for (Message message : messages) {

                Map<String, MessageAttributeValue> attr = message.getMessageAttributes();

                orderID = attr.get("orderID").getStringValue();
                productID = attr.get("productID").getStringValue();
                quantity = Integer.valueOf(attr.get("quantity").getStringValue());

                if (orderID.isEmpty() || productID.isEmpty() || quantity <= 0) {
                    String debug = orderID + ", " + productID + ", " + String.valueOf(quantity);
                    System.out.println("Invalid order was received: " + debug);

                    continue;
                }

                int supply = (int) ((Math.random()*10) + quantity);

                System.out.println("\tSupplying " + String.valueOf(supply) + " x " + productID);

                warehouse.addSupplies(productID, supply);               // add supplies to warehouse
                warehouse.placeOrder(orderID, productID, quantity);     // process pending order

                System.out.println("\tSupplied " + String.valueOf(supply) + " x " + productID);

                Thread.sleep(10000);

            }

        }

    }

}