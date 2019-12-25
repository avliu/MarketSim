import java.util.HashSet;

abstract class Transactor {

    public double max = 100.0;

    public double bound;
    public double expected_price;
    public double swing;
    public HashSet<Integer> ids;

    public Transactor(){};

    public Transactor(HashSet<Integer> ids) {
        bound = Math.random() * max;
        expected_price = Math.random() * max;
        swing = 0.0;
        this.ids = ids;
    }

    public static boolean transact(Buyer b, Seller s, double p) {
        if (b.satisfied(p) && s.satisfied(p)) {
            return true;
        }
        return false;
    }

    public double propose_price(){ return expected_price; }

    public static double transaction_surplus(Buyer b, Seller s, double p) {
        return b.surplus(p) + s.surplus(p);
    }

    abstract double surplus(double price);
    abstract boolean satisfied(double price);
    abstract void update_expected_price(double price);

}
