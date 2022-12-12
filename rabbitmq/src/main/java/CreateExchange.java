import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class CreateExchange {

    //Run me first
    public static void main(String[] args) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("ampq://guest:guest@localhost:5672/");
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("RequestDirectExchange", BuiltinExchangeType.DIRECT, true);

        //Close the channel and connection
        channel.close();
        connection.close();
    }
    
}
