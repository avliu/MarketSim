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
        swing = 2;
        this.ids = ids;
        desperate = false;
    }

    public Transactor(int id, HashSet<Integer> ids, double bound, double expected_price) {
        this.id = id;
        this.bound = bound;
        this.expected_price = expected_price;
        swing = 2;
        this.ids = ids;
        desperate = false;
    }


    public static double transact(Buyer b, Seller s) {
        b.ids.remove(s.id);
        s.ids.remove(b.id);
        double b_price = b.expected_price;
        double s_price = s.expected_price;
        if (b.desperate) b_price = b.acquiesce_price();
        if (s.desperate) s_price = s.acquiesce_price();
        if (s_price <= b_price) {
            // price of transaction
            return (b_price + s_price)/2;
        }
        else {
            return -1;
        }
    }

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

    public abstract double aggressive_price();
    public abstract double acquiesce_price();
    public void update_expected_price(boolean success) {
        if (success) {
            expected_price = aggressive_price();
        }
        else {
            expected_price = acquiesce_price();
        }
    }

    public void reset(HashSet<Integer> new_ids) {
        ids = new_ids;
        desperate = false;
    }

    abstract double surplus(double price);

}
