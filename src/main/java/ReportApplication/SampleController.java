package ReportApplication;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class SampleController {

    public javafx.scene.control.TextField textsfield;

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

    @FXML
    private void initialize() {
//        myWebView.getEngine().load(myUrl);
        TableColumn a = new TableColumn("Name"); //Make a new column
//        tableView.getColumns().add(a);
//        TextField textField = new TextField ();
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
    public void pressButton(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sample1.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            MyApp.stg.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/25395016/how-can-i-add-rows-and-columns-to-a-javafx-8-tableview
    public TableView<ArrayList<String>> readTabDelimitedFileIntoTable(Path file) throws IOException {
        TableView<ArrayList<String>> table = new TableView<>();
        Files.lines(file).map(line -> line.split("\t")).forEach(values -> {
            // Add extra columns if necessary:
            for (int i = table.getColumns().size(); i < values.length; i++) {
                TableColumn<ArrayList<String>, String> col = new TableColumn<>("Column "+(i+1));
                col.setMinWidth(80);
                final int colIndex = i ;
                col.setCellValueFactory(data -> {
                    ArrayList<String> rowValues = data.getValue();
                    String cellValue ;
                    if (colIndex < rowValues.size()) {
                        cellValue = rowValues.get(colIndex);
                    } else {
                        cellValue = "" ;
                    }
                    return new ReadOnlyStringWrapper(cellValue);
                });
                table.getColumns().add(col);
            }

            // add row:
            table.getItems().add(new ArrayList<>(Arrays.asList(values)));
        });
        return table ;
    }
}
