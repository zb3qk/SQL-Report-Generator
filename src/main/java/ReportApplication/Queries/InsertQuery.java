package ReportApplication.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class InsertQuery {

    final static Logger logger = Logger.getLogger(ExecuteQuery.class.getName());
    Connection conn;

    InsertQuery(ExecuteQuery exQ){
        this.conn = exQ.conn;
    }

    private String createListElements(ArrayList<String> list){
        StringJoiner join = new StringJoiner(", ");
        list.forEach((el)->join.add(el));
        return "("+join.toString()+")"; //TODO may need to remove parens if this doesnt work
    }

    private boolean setInsert(PreparedStatement p, int index, String dataType, Object data){
        try { //Add more DataTypes
            switch (dataType) {
                case "String":
                    p.setString(index, (String) data);
                    break;
                case "Int":
                    p.setInt(index, (int) data);
                    break;
                case "Boolean":
                    p.setBoolean(index, (boolean) data);
                    break;
                default:
                    System.out.println("Warning: DataType not Found, entry not entered");
                    return false;
            }
        } catch (SQLException e) {
            logger.warning("Bad DataType");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public PreparedStatement InsertStatement(String tableName, ArrayList<String> columns) {

        String columnComma = createListElements(columns);

        try {
            PreparedStatement insertStmt = conn
                    .prepareStatement("insert into " + tableName + " " + columnComma + " VALUES (?,?,?,?)");
            return insertStmt;
        } catch (SQLException e) { return null; }
    }

    public boolean ExecuteInsert (PreparedStatement pre, ArrayList<Object> entires, ArrayList<String> datatypes) {
        boolean bool = true;
        for(int i=0; i<entires.size(); i++){
            bool &= setInsert(pre, i, datatypes.get(i), entires.get(i));
        }
        return bool;
    }

}
