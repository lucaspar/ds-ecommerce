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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.ws.Endpoint;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.SecureRandom;
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

    @WebListener
    public static class InitializeListener implements ServletContextListener {

        @Override
        public final void contextInitialized(final ServletContextEvent sce) {
            // initialization code
        }

        @Override
        public final void contextDestroyed(final ServletContextEvent sce) {}
    }

    @WebMethod
    public String checkOrder(String orderID) throws RemoteException {

        if (warehouse == null) {
            System.out.println("The Web Service was not started correctly.");
            return "Não foi possível verificar o pedido";
        }

        return (warehouse.checkProcessed(orderID)) ?
                "Pedido processado." : "Pedido ainda não processado.";
    }

    @WebMethod
    public void init(String managerIP) {

        System.out.println("\t\t===============");
        System.out.println("\t\t  Web Service  ");
        System.out.println("\t\t===============\n");

        sqs         = connect2Amazon();
        warehouse   = connect2Manager(managerIP);
        pending     = sqs.listQueues().getQueueUrls().get(0);

    }

    @WebMethod
    public String placeOrder(String productID, int quantity) throws RemoteException {

        if (sqs == null || warehouse == null || pending == null) {
            System.out.println("The Web Service was not started correctly.");
            return "Não foi possível efetuar o pedido";
        }

        String orderID = new BigInteger(130, new SecureRandom()).toString(32);

        if (warehouse.placeOrder(orderID, productID, quantity)) {       // processed
            System.out.println("\tOrder " + orderID + " processed.");
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
                sqs.sendMessage(new SendMessageRequest(pending, "Order description")
                                .withMessageAttributes(orderAttributes));

                System.out.println("\tOrder " + orderID + " posted to pending queue");

                return orderID;

            } catch (AmazonServiceException ase) {
                serviceException(ase);

            } catch (AmazonClientException ace) {
                clientException(ace);
            }

        }

        return "";

    }

    public static void main(String[] args) {                // not used in Tomcat

        Object implementor = new EcommerceService();
        //String address = "http://192.168.248.151:9000/EcommerceService";
        String address = "http://localhost:9000/EcommerceService";
        Endpoint.publish(address, implementor);

        System.out.println("\t" + address);

    }

}
