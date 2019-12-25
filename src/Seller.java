import java.util.HashSet;

public class Seller extends Transactor{

    public Seller(HashSet<Integer> buyer_ids) {
        super(buyer_ids);
    }

    public double surplus(double price) {
        return price - bound;
    }

    public boolean satisfied(double price) {
        if(ids.size()>1){
            return price >= expected_price;
        }
        return price >= bound;
    }

    public void update_expected_price(double price){
        double new_expected_price = expected_price + (price-expected_price) * swing;
        if (new_expected_price < bound) {
            new_expected_price = bound;
        }
        expected_price = new_expected_price;
    }
}
