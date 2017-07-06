package example;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@WebService()
public class EcommerceService {

    private static AmazonSQS sqs;
    private static String pending;

    @WebMethod
    public String sayHelloWorldFrom(String from) {

        String result = "Hello, world, from " + from;
        System.out.println(result);
        return result;

    }

    @WebMethod
    public String serverTime() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());

    }

    @WebMethod
    public String checkOrder(String orderID) {
        return "TODO. Order ID: " + orderID;
    }

    @WebMethod
    public String placeOrder(String productID, int quantity) {

        String orderID = UUID.randomUUID().toString();

        /*
        TupleSpace inventory;
        TupleSpace processed;
        if ( inventory.take(<productID, quantity>) )    // Product available, take from inventory and send to processed
            processed.put(<orderID, productID, quantity>);

        }
        else {  */                                      // Product unavailable, send to pending queue
            System.out.println("Posting order to pending queue...");

            // build message
            Map<String, MessageAttributeValue> orderAttributes = new HashMap<>();
            orderAttributes.put("orderID", new MessageAttributeValue().withDataType("String").withStringValue(orderID));
            orderAttributes.put("productID", new MessageAttributeValue().withDataType("String").withStringValue(productID));
            orderAttributes.put("quantity", new MessageAttributeValue().withDataType("Number").withStringValue(Integer.toString(quantity)));

            try {
                SendMessageResult result = sqs
                        .sendMessage(new SendMessageRequest(pending, "Order description")
                                .withMessageAttributes(orderAttributes));

                System.out.println(result.toString());

                return orderID;

            } catch (AmazonServiceException ase) {
                serviceException(ase);

            } catch (AmazonClientException ace) {
                clientException(ace);
            }

        //}

        return "";

    }

    private static void clientException(AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException: failed communicating with SQS");
        System.out.println("Error Message: " + ace.getMessage());
    }

    private static void serviceException(AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException:");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    }

    public static void main(String[] argv) {

        connect2Amazon();

        startWebService();

    }

    private static void connect2Amazon() {

        AWSCredentials awsCredentials;
        DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();

        try {
            awsCredentials = credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

        System.out.println("\t\t\t======================");
        System.out.println("\t\t\t| Started Amazon SQS |");
        System.out.println("\t\t\t======================\n");

        try {
            // Get pending orders queue URL
            pending = sqs.listQueues().getQueueUrls().get(0);
            System.out.println("\tQueueUrl: " + pending);

        } catch (AmazonServiceException ase) {
            serviceException(ase);

        } catch (AmazonClientException ace) {
            clientException(ace);
        }

    }

    private static void startWebService() {

        Object implementor = new EcommerceService();
        //String address = "http://192.168.0.104:9000/HelloWorld";
        String address = "http://localhost:9000/EcommerceService";
        Endpoint.publish(address, implementor);

    }
}
