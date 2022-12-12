import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;


public class CreateQueue {
    
    //Run me second
    public static void main(String[] args) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("ampq://guest:guest@localhost:5762/");
        Channel channel = connection.createChannel();

        channel.queueDeclare("RequestQueue", true, false, false, null);
        
        //Close the channel and connection
        channel.close();
        connection.close();
    } 
}
