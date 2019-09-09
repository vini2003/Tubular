package tubular.utility;

public class Tuple<X, Y> {
    public X first;
    public Y second;
    
    public Tuple(X x, Y y) {
        this.first = x;
        this.second = y;
    }

    public X getFirst() {
        return this.first;
    }

    public Y getSecond() {
        return this.second;
    }

    public void setFirst(X x) {
        this.first = x;
    }

    public void setSecond(Y y) {
        this.second = y;
    }
}