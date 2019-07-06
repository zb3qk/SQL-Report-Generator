package ReportApplication;

import ReportApplication.Queries.ExecuteLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class MyApp extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;
    static Stage currentStage;
    static ExecuteLogin login; //Universal reference to login
    static Properties prop;
    String properties = "hw-properties.properties";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(MyApp.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        prop = new Properties();
        setUpProperties(prop);
        if(!autoLogin()) {
            fxmlLoader.setLocation(getClass().getResource("/fxml/login.fxml"));
            rootNode = fxmlLoader.load();
            this.currentStage = primaryStage;
            primaryStage.setTitle("Hello World");
            Scene scene = new Scene(rootNode, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    @Override
    public void stop() {
        springContext.stop();
    }

    /**
     * Executes the Automatic login using the Properties file referenced in the origin MyApp class
     */
    private boolean autoLogin(){
        if(prop.getProperty("db.autologin").equals("true")){
            try {
                MyApp.login = new ExecuteLogin(prop);
                loadHomePage();
                return true;
            } catch (Exception e) {
                System.out.println("Automatic login failed, please log in again");
            }
        }
        return false;
    }

    public void loadHomePage(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/table.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm()); // loads css
//            scene.getStylesheets().add("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"); // loads css
            stage.setScene(scene);

            stage.show();
//            MyApp.currentStage.close();

            MyApp.currentStage = stage; //Setup new Stage
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in loading the Home Page");
        }
    }

//    public void loadNewPage(String fxml){
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/table.fxml"));
//        Parent root = (Parent) fxmlLoader.load();
//
//        Stage stage = new Stage();
//        Scene scene = new Scene(root, 800, 600);
//        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm()); // loads css
//        stage.setScene(scene);
//
//        stage.show();
//        MyApp.currentStage.close();
//
//        MyApp.currentStage = stage; //Setup new Stage
//    }

    /**
     * Acquires the Properties file
     * @param prop
     */
    private void setUpProperties(Properties prop){
        try (InputStream input = new FileInputStream(properties)) {
            prop.load(input);
        } catch (Exception e){
            try {
                FileWriter f = new FileWriter(properties);
                f.flush();
                InputStream input = new FileInputStream(properties);
                prop.load(input);
            } catch (Exception ex){
                System.out.println("Bad Propreties file, please re-install the application");
            }
        }
    }

}