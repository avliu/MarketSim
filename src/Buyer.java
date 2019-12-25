import java.util.HashSet;

public class Buyer extends Transactor{

    public double surplus(double price) {
        return bound - price;
    }

    public Buyer(int id, HashSet<Integer> seller_ids){
        super(id, seller_ids);
        bound = Math.random() * max/2 + max/2;
        expected_price = Math.random() * bound;
    }

    public boolean satisfied (double price) {
        if (!desperate){
            return price <= expected_price;
        }
        return price <= bound;
    }

}
