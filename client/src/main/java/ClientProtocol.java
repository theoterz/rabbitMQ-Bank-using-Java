import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientProtocol 
{
    Scanner user = new Scanner(System.in);

    public Request getInput()
    {
        //Creates user class and gets username and password

        Request r = new Request();

        System.out.println("\nUsername:");
        String username = user.nextLine();
        r.setData1(username);
        System.out.println("\nPassword:");
        String password = user.nextLine();
        r.setData2(password);
        
        r.setTransaction(0);

        return r;
    }

    public int getOption()
    {
        printMenu();

        System.out.println("Choose Action(1-4):");
        int input = user.nextInt();

        //input check
        while( !(input>0 && input<5) )
        {
            System.out.println("Choose Action(1-4)");
            input = user.nextInt();
        }

        return input;
    }

    public Request prepareRequest(int input, int id)
    {
        //detrmines the desired action and creates action class

        double amm;


        switch(input)
        {
            case(1):
                System.out.println("\nEnter Ammount");
                amm = user.nextDouble();
                return new Request(1,Integer.toString(id),Double.toString(amm));
            case(2):
                System.out.println("\nEnter Ammount");
                amm = user.nextDouble();
                return new Request(2,Integer.toString(id),Double.toString(amm));
            default:
                return new Request(3,Integer.toString(id),Double.toString(0));
        }
    }

    public void printMenu()
    {
        System.out.println("\n=======================");
        System.out.println("1.Withdrawal");
        System.out.println("2.Deposit");
        System.out.println("3.Show Balance");
        System.out.println("4.Close");
        System.out.println("=======================\n");
    }

    public byte[] getByteArray(Request r) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(r);
		return out.toByteArray();
    }
    
}
