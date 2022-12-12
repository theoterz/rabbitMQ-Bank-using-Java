import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class CreateBinding {

    //Run me thrird
    public static void main(String[] args) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("ampq://guest:guest@localhost:5762/");
        Channel channel = connection.createChannel();

        channel.queueBind("RequestQueue", "RequestDirectExchange", "requestQueueBind");

        //Close the channel and connection
        channel.close();
        connection.close();
    } 
    
}
