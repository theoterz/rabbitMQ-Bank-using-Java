import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Server
{
    public static void main(String[] args) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("ampq://guest:guest@localhost:5762/");
        final Channel channel = connection.createChannel();

        final DbCon dbConnection = new DbCon();
        dbConnection.doConnection();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();
            
            byte[] byteArray = delivery.getBody();

            try {
                Request r = (Request) deserialize(byteArray);
                String response = "";
                
                if(r.getTransaction()==0)
                {
                    String username = r.getData1();
                    String password = r.getData2();
                    response = Integer.toString(dbConnection.authenticateUser(username, password));
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                }else
                {
                    int id = Integer.parseInt(r.getData1());
                    double ammount = Double.parseDouble(r.getData2());

                    switch(r.getTransaction())
                    {
                        case(1):
                            response = dbConnection.withdrawal(ammount, id);
                            break;
                        case(2):
                            response = dbConnection.deposit(ammount, id);
                            break;
                        default:
                            response = dbConnection.showBalance(id);
                            break;
                    }

                }
                
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                
            } catch (ClassNotFoundException  | SQLException e) {
                e.printStackTrace();
            }
        };

        channel.basicConsume("RequestQueue", true, deliverCallback, (consumerTag -> {}));
       
    }

    private static Object deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
    }
}
