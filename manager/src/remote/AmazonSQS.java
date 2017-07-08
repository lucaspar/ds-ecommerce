package remote;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AmazonSQS {

    public static com.amazonaws.services.sqs.AmazonSQS connect2Amazon() {

        com.amazonaws.services.sqs.AmazonSQS sqs;
        AWSCredentials awsCredentials;
        DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();

        try {
            awsCredentials = credentialsProvider.getCredentials();
        }
        catch (Exception e) {
            throw new AmazonClientException(
                "Cannot load the credentials from the credential profiles file. " +
                "Please make sure that your credentials file is at the correct " +
                "location (~/.aws/credentials), and is in valid format.",
                e);
        }

        sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

        try {                   // test
            sqs.listQueues();
            System.out.println("\t:: Connected to Amazon SQS ::\n");

        } catch (AmazonServiceException ase) {
            serviceException(ase);

        } catch (AmazonClientException ace) {
            clientException(ace);
        }

        return sqs;

    }

    public static void clientException(AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException: failed communicating with SQS");
        System.out.println("Error Message: " + ace.getMessage());
    }

    public static void serviceException(AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException:");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    }

}
