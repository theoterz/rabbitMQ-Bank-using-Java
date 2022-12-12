import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCon 
{
    Connection connection = null;
    Statement statement = null;
    String driverName = "org.postgresql.Driver";
    String url="jdbc:postgresql://localhost:5432/bank";
    String username = "postgres";
    String password = "";

    int id;
    String  usrname;
    String  pswrd;
    Double  balance;

    public DbCon() {}

    public boolean doConnection() 
    {
        try {
            //Load PostgreSQL JDBC Driver 
            Class.forName(driverName);

            connection = DriverManager.getConnection(url, username, password);

        } catch(ClassNotFoundException e) {
            // Could not find the database driver
            System.out.println("ClassNotFoundException : "+e.getMessage());
            return false;

        } catch (SQLException e) {
            //Could not connect to the database
            System.out.println(e.getMessage());
            return false;  
        }
        
        return true;
    }

    public synchronized int authenticateUser(String username2, String password2) throws SQLException
    {    
        //checks if the username and password given by the user are correct
        
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery( "SELECT * FROM users;" );
        while ( rs.next() ) {
            id = rs.getInt("id");
            usrname = rs.getString("username");
            pswrd  = rs.getString("password");
            if(usrname.equals(username2) && pswrd.equals(password2))
            {
                return id;
            }
        }
        return -1;
    }

    public synchronized String withdrawal(double ammount, int id) throws SQLException
    {
        String result = "";
        
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery( "SELECT * FROM users where id="+id+" FOR UPDATE;" );
        while(rs.next())
        {
            balance = rs.getDouble("balance");
        }
       
        if(ammount>balance) result = "Balance is not enough!";
        else if(balance>=ammount)
        {
            balance = balance - ammount;
            statement = connection.createStatement();
            String sql = "UPDATE users SET balance = "+balance +" where id=" +id +";";
            statement.executeUpdate(sql);
            result = "Success! New Balance: " + balance;
        }

        return result;
    }

    public synchronized String deposit(double ammount, int id) throws SQLException
    {
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery( "SELECT * FROM users where id="+id+" FOR UPDATE;" );
        while(rs.next())
        {
            balance = rs.getDouble("balance");
        }
        
        balance = balance + ammount;
        statement = connection.createStatement();
        String sql = "UPDATE users SET balance = "+balance +" where id=" +id +";";
        statement.executeUpdate(sql);
        return "Success! New Balance: " + balance;
    }

    public synchronized String showBalance(int id) throws SQLException {
        
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery( "SELECT * FROM users where id="+id+";" );
        while(rs.next())
        {
            balance = rs.getDouble("balance");
        }
        
        return "Balance: " + balance;
    }
}
