package ReportApplication.Queries;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class ExecuteQuery {

final static Logger logger = Logger.getLogger(ExecuteQuery.class.getName());
Connection conn;
ExecuteLogin login;

    ExecuteQuery(Connection con){
        this.conn = con;
    }

    public ExecuteQuery(ExecuteLogin log){
        this.login = log;
        this.conn = log.con;
    }

    private String createListElements(ArrayList<String> list){
        StringJoiner join = new StringJoiner(", ");
        list.forEach((el)->join.add(el));
        return "("+join.toString()+")";
    }

    public ArrayList<ArrayList> getColumns(String tableName){
        try {
            ArrayList<String> cols = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();

            DatabaseMetaData md = conn.getMetaData();
            ResultSet resultSet = md.getColumns(null, null, tableName, null);
            while (resultSet.next()){
                cols.add(resultSet.getString("COLUMN_NAME"));
                types.add(resultSet.getString("TYPE_NAME"));
            }
            ArrayList<ArrayList> colAndType = new ArrayList<>();
            colAndType.add(cols); colAndType.add(types);
            return colAndType;
        } catch (SQLException e){
            System.out.println("ERROR: Could  not retrieve Column Names");
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getTableList(){
        try {
            ArrayList tables = new ArrayList<String>();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
            return tables;
        }
        catch (SQLException e){
            System.out.println("ERROR: Could not retrieve Tables");
            e.printStackTrace();
            return null;
        }
    }
    public ExecuteLogin getLogin(){
        return this.login;
    }

}
