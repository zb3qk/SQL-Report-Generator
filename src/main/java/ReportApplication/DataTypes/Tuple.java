package ReportApplication.DataTypes;

public class Tuple {
    Object x;
    Object y;

    public Tuple(Object x, Object y){
        this.x = x;
        this.y = y;
    }

    public Object getX(){
        return x;
    }

    public Object getY(){
        return y;
    }
}
