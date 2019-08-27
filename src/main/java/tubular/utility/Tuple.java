package tubular.utility;

public class Tuple<X, Y> {
    public X x;
    public Y y;
    
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getFirst() {
        return this.x;
    }

    public Y getSecond() {
        return this.y;
    }

    public void setFirst(X x) {
        this.x = x;
    }

    public void setSecond(Y y) {
        this.y = y;
    }
}