package ReportApplication;

import ReportApplication.Queries.ExecuteLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class LoginPage {

    String properties = "hw-properties.properties";
    public javafx.scene.control.TextField textsfield;
    public javafx.scene.control.TextField database;
    public javafx.scene.control.TextField username;
    public javafx.scene.control.TextField password;
    Properties prop;

    public Label message;
    public CheckBox autoLogin;

    @FXML
    TextField textfield;

    @FXML
    public Label helloWorld;

    @FXML
    public TableView tableView;

    @FXML
    private WebView myWebView;

    @Value("${my.url}")
    private String myUrl;

    public LoginPage(){

    }

    //TODO set up all 'url' variables to be under one textfield

    @FXML
    private void initialize() {
//        myWebView.getEngine().load(myUrl);
        prop = MyApp.prop; //needed here to follow JavaFX compilation pipeline
    }

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hallo World");
    }

    public void getLabel(){
        String text = textsfield.getText();
        System.out.println(text);
        helloWorld.setText(text);
    }

    //https://stackoverflow.com/questions/43086875/javafx-8-on-button-click-show-other-scene/43087141
    /**
     * Loads into the logged in homepage
     */
    public void pressButton(){
        MyApp app = new MyApp();
        app.loadHomePage();
    }

    /**
     * Log into the database using the GUI defined inputs
     * @param event
     */
    public void login(ActionEvent event) {
        System.out.println("Attempt");
        String url = "localhost";
        message.setText("Querying ... ");
        try {
            MyApp.login = new ExecuteLogin(url, database.getText(), username.getText(), password.getText());
            message.setText("Login Successful");
        } catch (Exception e) {
            message.setText("Login failed, please try again");
            message.setTextFill(Color.rgb(210, 39, 30));
        }
            setUpAutologin(autoLogin.isSelected());
            pressButton();
    }

    /**
     * Inputs username, database, and password information into the properties file
     * @param tf Whether the automatic login is selected or not in the GUI
     */
    private void setUpAutologin(boolean tf){
        System.out.println(tf);
        String url = "localhost";
        System.out.println(prop);
        if (tf) {
            prop.setProperty("db.autologin", "true");
            prop.setProperty("db.user", username.getText());
            prop.setProperty("db.password", password.getText());
            prop.setProperty("db.url", "jdbc:mysql://"+url+""+"/"+database.getText()+"?allowPublicKeyRetrieval=true&useSSL=false");
            System.out.println("Here!");
        }
        else {
            prop.setProperty("db.autologin", "false");
        }
    }

}
