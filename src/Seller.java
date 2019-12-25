import java.util.HashSet;

public class Seller extends Transactor{

    public Seller(int id, HashSet<Integer> buyer_ids) {
        super(id, buyer_ids);
        bound = Math.random() * max/2;
        expected_price = Math.random() * (max-bound) + bound;
    }

    public double surplus(double price) {
        return price - bound;
    }

    public boolean satisfied(double price) {
        if (!desperate){
            return price >= expected_price;
        }
        return price >= bound;
    }

}
