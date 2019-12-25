import java.util.HashSet;

public class Buyer extends Transactor{

    public double surplus(double price) {
        return bound - price;
    }

    public Buyer(HashSet<Integer> seller_ids){
        super(seller_ids);
    }

    public boolean satisfied(double price) {
        if(ids.size()>1){
            return price <= expected_price;
        }
        return price <= bound;
    }

    public void update_expected_price(double price){
        double new_expected_price = expected_price + (price-expected_price) * swing;
        if (new_expected_price > bound) {
            new_expected_price = bound;
        }
        expected_price = new_expected_price;
    }

}
