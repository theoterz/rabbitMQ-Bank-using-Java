import java.io.Serializable;

public class Request implements Serializable {

    
    int transaction;
    String data1;
    String data2;

    public Request(int transaction, String data1, String data2) 
    {
        this.transaction = transaction;
        this.data1 = data1;
        this.data2 = data2;
    }

    public Request() {};

    public int getTransaction() {
        return transaction;
    }

    public String getData1() {
        return data1;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData2() {
        return data2;
    }

    
}
