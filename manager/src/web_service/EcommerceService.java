package web_service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import manager.ManagerInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static remote.AmazonSQS.clientException;
import static remote.AmazonSQS.connect2Amazon;
import static remote.AmazonSQS.serviceException;
import static remote.ConnectManager.connect2Manager;


@WebService()
public class EcommerceService {

    private static AmazonSQS sqs;
    private static String pending;
    private static ManagerInterface warehouse;

    @WebMethod
    public String checkOrder(String orderID) throws RemoteException {
        return (warehouse.checkProcessed(orderID)) ?
                "Pedido processado." : "Pedido pendente.";
    }

    @WebMethod
    public String placeOrder(String productID, int quantity) throws RemoteException {

        String orderID = UUID.randomUUID().toString();

        if (warehouse.placeOrder(orderID, productID, quantity)) {       // processed
            return orderID;
        }
        else {                                                          // pending

            // build message
            Map<String, MessageAttributeValue> orderAttributes = new HashMap<>();
            orderAttributes.put("orderID", new MessageAttributeValue().withDataType("String").withStringValue(orderID));
            orderAttributes.put("productID", new MessageAttributeValue().withDataType("String").withStringValue(productID));
            orderAttributes.put("quantity", new MessageAttributeValue().withDataType("Number").withStringValue(Integer.toString(quantity)));

            // send message
            try {
                SendMessageResult result = sqs
                        .sendMessage(new SendMessageRequest(pending, "Order description")
                                .withMessageAttributes(orderAttributes));

                System.out.println("\tOrder " + orderID + " posted to pending queue");
                System.out.println("\tSQS: " + result.toString());

                return orderID;

            } catch (AmazonServiceException ase) {
                serviceException(ase);

            } catch (AmazonClientException ace) {
                clientException(ace);
            }

        }

        return "";

    }

    public static void main(String[] argv) {

        System.out.println("\t\t===============");
        System.out.println("\t\t  Web Service  ");
        System.out.println("\t\t===============\n");

        warehouse   = connect2Manager();
        sqs         = connect2Amazon();
        pending     = sqs.listQueues().getQueueUrls().get(0);

        startWebService();

    }

    private static void startWebService() {

        Object implementor = new EcommerceService();
        //String address = "http://192.168.248.151:9000/EcommerceService";
        String address = "http://localhost:9000/EcommerceService";
        Endpoint.publish(address, implementor);

        System.out.println("\t" + address);

    }

}
