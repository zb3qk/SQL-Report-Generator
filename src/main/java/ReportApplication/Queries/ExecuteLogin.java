package ReportApplication.Queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class ExecuteLogin {
    Connection con;
    String url;
    String user;
    String password;
    Properties prop;

    public ExecuteLogin(Properties prop) throws Exception {
        this.prop = prop;
        url = prop.getProperty("db.url");
        user = prop.getProperty("db.user");
        password = prop.getProperty("db.password");
        connect();
    }

    public ExecuteLogin(String url, String db, String user, String password) throws Exception{
        // Get input for login information
//        scannerLogin();
        this.url = "jdbc:mysql://"+url+""+"/"+db+"?allowPublicKeyRetrieval=true&useSSL=false";
        this.user = user;
        this.password = password;
        connect();
    }

    private void saveLoginInformation(){
        prop.setProperty("db.url",url);
        prop.setProperty("db.password",password);
        prop.setProperty("db.user",user);
    }

    private void clearLoginInformation(){
        prop.setProperty("db.url","");
        prop.setProperty("db.password","");
        prop.setProperty("db.user","");
    }

    private void scannerLogin() throws Exception{
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter datababse url");
        String thisUrl = myObj.nextLine();  // Read user input
        System.out.println("Enter datababse name");
        String db = myObj.nextLine();  // Read user input

        this.url = "jdbc:mysql://"+thisUrl+""+"/"+db+"?allowPublicKeyRetrieval=true&useSSL=false";
        System.out.println("Enter datababse username");
        this.user = myObj.nextLine();  // Read user input
        System.out.println("Enter datababse password");
        this.password = myObj.nextLine();  // Read user input
    }

    private void connect() throws Exception{
            con = DriverManager.getConnection(url, user, password);
//            System.out.println("Successfully Connected!");
    }

    public String getUser() {
        return user;
    }

    public Connection getCon(){
            return con;
    }
}
