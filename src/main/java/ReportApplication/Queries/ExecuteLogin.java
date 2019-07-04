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

    public ExecuteLogin(Properties prop) {
        url = prop.getProperty("db.url");
        user = prop.getProperty("db.user");
        password = prop.getProperty("db.password");
        connect();
    }

    public ExecuteLogin(String url, String db, String user, String password) {
        // Get input for login information
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter datababse url");
        String thisUrl = myObj.nextLine();  // Read user input
        System.out.println("Enter datababse name");
        db = myObj.nextLine();  // Read user input

        this.url = "jdbc:mysql://"+thisUrl+""+"/"+db+"?allowPublicKeyRetrieval=true&useSSL=false";
        System.out.println("Enter datababse username");
        this.user = myObj.nextLine();  // Read user input
        System.out.println("Enter datababse password");
        this.password = myObj.nextLine();  // Read user input
        connect();
    }

    private void connect() {
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Successfully Connected!");
        } catch (SQLException ex) {
            System.out.println("Failed to Connect ...");
            System.out.println(ex);
            // see what happens if you edit code above to give bad user name, bad password, bad database, etc.
        }
    }

    public String getUser() {
        return user;
    }

    public Connection getCon(){
            return con;
    }
}
