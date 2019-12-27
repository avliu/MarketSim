import java.util.HashSet;

public class Buyer extends Transactor{

    public double surplus(double price) {
        return bound - price;
    }
    public double aggressive_price() {
//        return expected_price - (expected_price * swing);
        double new_price = expected_price - swing;
        if (new_price < 0) {
            return 0;
        }
        else {
            return new_price;
        }
    }
    public double acquiesce_price() {
//        return expected_price + ((bound - expected_price) * swing);
        double new_price = expected_price + swing;
        if (new_price > bound) {
            return bound;
        }
        else {
            return new_price;
        }
    }

    public Buyer(int id, HashSet<Integer> seller_ids){
        super(id, seller_ids);
        bound = Math.random() * max/2 + max/2;
        expected_price = Math.random() * (bound-max/2) + max/2;
    }

    public Buyer(int id, HashSet<Integer> ids, double bound, double expected_price) {
        super(id, ids, bound, expected_price);
    }

}
