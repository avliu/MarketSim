import java.util.HashSet;

public class Seller extends Transactor{

    public double surplus(double price) {
        return price - bound;
    }
    public double aggressive_price() {
        double new_price = expected_price + swing;
        if (new_price > max) {
            return max;
        }
        else {
            return new_price;
        }
    }
    public double acquiesce_price() {
        double new_price = expected_price - swing;
        if (new_price < bound) {
            return bound;
        }
        else {
            return new_price;
        }
    }

    public Seller(int id, HashSet<Integer> buyer_ids) {
        super(id, buyer_ids);
        bound = Math.random() * max/2;
        expected_price = Math.random() * (max/2-bound) + bound;
    }

    public Seller(int id, HashSet<Integer> ids, double bound, double expected_price) {
        super(id, ids, bound, expected_price);
    }

}
