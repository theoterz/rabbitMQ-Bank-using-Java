import com.rabbitmq.client.AMQP;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.UUID;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Client {
    
    static ConnectionFactory factory;
    static Connection connection;
    static Channel channel;
    static ClientProtocol protocol = new ClientProtocol();
    
    public static void main(String[] args) throws IOException, TimeoutException, NumberFormatException, InterruptedException, ExecutionException
    {
        factory = new ConnectionFactory();
        connection = factory.newConnection("ampq://guest:guest@localhost:5762/");
        channel = connection.createChannel();
        
        int id = -1;

        while(id == -1)
        {

            id = login();
            
        }

        int input = protocol.getOption();
        
        while(input != 4)
        {
            System.out.println(call(input, id));
            input = protocol.getOption();
        }
        
        
        //Close the channel and connection
        channel.close();
        connection.close();
    } 

    static public int login() throws IOException, NumberFormatException, InterruptedException, ExecutionException
    {

        String corrId = UUID.randomUUID().toString();
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();

    
        CompletableFuture<String> response = new CompletableFuture<>();
        byte[] byteArray = protocol.getByteArray(protocol.getInput());
        channel.basicPublish("RequestDirectExchange", "requestQueueBind", props, byteArray);

        channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {});
        
        return Integer.parseInt(response.get());
    }

    static public String call(int input, int id) throws IOException, InterruptedException, ExecutionException
    {
        String replyQueueName = channel.queueDeclare().getQueue();
        String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();
        
        CompletableFuture<String> response = new CompletableFuture<>();

        Request request = protocol.prepareRequest(input, id);
        byte[] byteArray = protocol.getByteArray(request);
        channel.basicPublish("RequestDirectExchange", "requestQueueBind", props, byteArray);

        channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.complete(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {});
            
        return response.get();
    }
}
