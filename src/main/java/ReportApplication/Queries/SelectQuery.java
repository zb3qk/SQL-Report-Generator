package ReportApplication.Queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class SelectQuery {

    final static Logger logger = Logger.getLogger(ExecuteQuery.class.getName());
    Connection conn;
    String tableName;
    ArrayList<ArrayList> columnsAndTypes; /// [column names, column data types]

    public SelectQuery(ExecuteQuery exQ, String tableName){
        this.tableName = tableName;
        this.conn = exQ.conn;
        this.columnsAndTypes = exQ.getColumns(tableName);
    }

    private String createListElements(ArrayList<String> list){
        StringJoiner join = new StringJoiner(", ");
        list.forEach((el)->join.add(el));
        return join.toString();
    }

    private ArrayList<String> setSelect(ArrayList<String> colNames){
        ArrayList allTypes = columnsAndTypes.get(1);
        ArrayList allCols = columnsAndTypes.get(0);

        ArrayList dataTypes = new ArrayList();
        colNames.forEach((col)->dataTypes.add
                (allTypes.get
                        (allCols.indexOf
                                (col))));
        return dataTypes; //returns DataType in order
    }

    private Object selectExecute(ResultSet rs, String dataType, String col) {
        try { //Add more DataTypes
            switch (dataType) {
                case "VARCHAR":
                    return rs.getString(col);
                case "TEXT":
                    return rs.getString(col);
                case "INT":
                    return rs.getInt(col);
                case "BIT":
                    return rs.getBoolean(col);
                default:
                    System.out.println("Warning: DataType not Found, entry not entered");
                    return null;
            }
        } catch (SQLException e) {
            logger.warning("Bad DataType");
            e.printStackTrace();
            return null;
        }
    }

    private String objectToString(Object ob) {
        return String.valueOf(ob);
    }

    public ArrayList<ArrayList> SelectStatement(ArrayList<String> columns,
                                                ArrayList<String> conditionalList, ArrayList<String> conditionals) {
        String columnComma = createListElements(columns);
        String query = ("select " + columnComma + " from " + tableName + ";"); //TODO Add conditionals
        return SelectStatementHelper(columns, query, conditionalList, conditionals);

    }

    public ArrayList<ArrayList> toPureStringList(ArrayList<ArrayList> list){
        ArrayList<ArrayList<String>> newList = new ArrayList<>();
        for (ArrayList arr: list){
            ArrayList<String> curArr = new ArrayList<>();
            for (Object el: curArr){
                Class elClass = el.getClass();

            }
            newList.add(curArr);
        }
        return null;
    }

    /**
     *  " select * from tableName "
     * @param conditionalList
     * @param conditionals
     * @return
     */
    public ArrayList<ArrayList> SelectStatement(ArrayList<String> conditionalList, ArrayList<String> conditionals) {
        String columnComma = createListElements(columnsAndTypes.get(0));
        String query = ("select * from " + tableName + ";"); //TODO Add conditionals
        return SelectStatementHelper(columnsAndTypes.get(0), query, conditionalList, conditionals);
    }

    public ArrayList<ArrayList> SelectStatementHelper(ArrayList<String> columns, String query,
                                                      ArrayList<String> conditionalList, ArrayList<String> conditionals) {
        String columnComma = createListElements(columns);
        try {
            Statement selectStmt = conn.createStatement();
            ArrayList<ArrayList> output = new ArrayList<>();
            for (String col: columns) {
                output.add(new ArrayList<>());
            }

            ResultSet rs = selectStmt.executeQuery(query);

            ArrayList<String> dataTypes = setSelect(columns);

            ArrayList<ArrayList> rows = new ArrayList<>();
            while (rs.next()){
                ArrayList curRow = new ArrayList();
                for (int i = 0; i<columns.size(); i++){
                    curRow.add(objectToString(selectExecute(rs, dataTypes.get(i),columns.get(i)))); /// TODO Added method to convert each element in row to String
                }
                rows.add(curRow);
            }
            return rows;
        } catch (SQLException e) {
            logger.warning("Bad Select Statement");
            e.printStackTrace();
            return null;
        }
    }

}
