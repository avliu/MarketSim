import java.util.HashSet;

abstract class Transactor {

    public Integer id;
    public final double max = 100.0;
    public double bound;
    public double expected_price;
    public double swing;
    public HashSet<Integer> ids;
    public boolean desperate;

    public Transactor(){};

    public Transactor(int id, HashSet<Integer> ids) {
        this.id = id;
        swing = 0.1;
        this.ids = ids;
        desperate = false;
    }



    public static boolean transact(Buyer b, Seller s, double p) {
        b.ids.remove(s.id);
        s.ids.remove(b.id);
        if (b.satisfied(p) && s.satisfied(p)) {
            return true;
        }
        return false;
    }

    public static double propose_price(Seller s){ return s.expected_price; }

    public static double transaction_surplus(Buyer b, Seller s, double p) {
        return b.surplus(p) + s.surplus(p);
    }

    public boolean update_status(HashSet<Integer> new_ids) {
        if (!desperate) {
            desperate = true;
            ids = new_ids;
            return false;
        }
        return true;
    }

    public void update_expected_price(double price, boolean success){
        if (success) {
            expected_price = expected_price + (price-expected_price) * swing;
        }
        else{
            expected_price = expected_price + (bound-expected_price) * swing;
        }
    }

    public void reset(HashSet<Integer> new_ids) {
        ids = new_ids;
        desperate = false;
    }

    abstract double surplus(double price);
    abstract boolean satisfied(double price);

}
