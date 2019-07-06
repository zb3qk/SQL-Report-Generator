package ReportApplication;

import ReportApplication.DataTypes.AutoCompleteTextField;
import ReportApplication.Queries.ExecuteQuery;
import ReportApplication.Queries.SelectQuery;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

@Controller
public class TableViewPage {

    public GridPane gridpane;
    ExecuteQuery quer;


    public TableView tableView;
    public AutoCompleteTextField autocomp;

    @Value("${my.url}")
    private String myUrl;

    @FXML
    private void initialize() {
//        myWebView.getEngine().load(myUrl);
        TableColumn a = new TableColumn("Name"); //Make a new column
        quer = new ExecuteQuery(MyApp.login);
//        tableView.getColumns().add(a);
//        TextField textField = new TextField ();

//        gridpane.getChildren().add(new javafx.scene.control.Button("Hallo"));

        AutoCompleteTextField text = new AutoCompleteTextField();
        text.setPrefColumnCount(0);
        gridpane.getChildren().add(text);
        text.getEntries().addAll(quer.getTableList());
        autocomp = text;

        // https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
        autocomp.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                {
                    System.out.println("Textfield on focus");
                    executeTableQuery(autocomp.getText());

                }
                else
                {
                    System.out.println("Textfield out focus");
                }
            }
        });
    }

    private void executeTableQuery(String tableName){
        SelectQuery selectQuery = new SelectQuery(quer, tableName);

        ArrayList<String> columns = quer.getColumns(tableName).get(0);
        try {
            ArrayList<ArrayList> rows = select(selectQuery, columns);
            autocomp.setStyle("-fx-control-inner-background: green");
            fillTable(rows, columns);

        } catch (Exception e){
            autocomp.setStyle("-fx-control-inner-background: red");
        }
    }


    private void fillTable(ArrayList<ArrayList> rows, ArrayList<String> columns){
        tableView.getItems().clear(); //clear all the entries in the table

        // Add columns
        int numColumns = rows.size();
        ArrayList<TableColumn> tableColumns = new ArrayList<>();
        for (String col: columns){
            tableColumns.add(new TableColumn<>(col));
        }
        tableView.getColumns().addAll(tableColumns);

        // Populate table
        //https://stackoverflow.com/questions/47885549/how-to-populate-tableview-with-string-arraylist-in-javafx
        for (ArrayList row: rows){
            int i=0;
            for (TableColumn col: tableColumns){
                System.out.println(col);
                col.setCellValueFactory(c -> {
                    return new SimpleStringProperty((String) row.get(i)); //Fix lambda function
                });
                i++;
            }
        }
    }

    //https://stackoverflow.com/questions/25395016/how-can-i-add-rows-and-columns-to-a-javafx-8-tableview
    public TableView<ArrayList<String>> readTabDelimitedFileIntoTable(Path file) throws IOException {
        Files.lines(file).map(line -> line.split("\t")).forEach(values -> {
            // Add extra columns if necessary:
            for (int i = tableView.getColumns().size(); i < values.length; i++) {
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
                tableView.getColumns().add(col);
            }

            // add row:
            tableView.getItems().add(new ArrayList<>(Arrays.asList(values)));
        });
        return tableView ;
    }

    public static ArrayList<ArrayList> select(SelectQuery select, ArrayList<String> cols) throws Exception{
        return select.SelectStatement(cols, null, null);
    }
}
