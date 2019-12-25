import java.util.*;

public class Market {

    public int buyer_size;
    public int seller_size;
    public HashMap<Integer, Buyer> buyers;
    public HashMap<Integer, Seller> sellers;

    public Market(int num_buyers, int num_sellers) {

        buyer_size = num_buyers;
        seller_size = num_sellers;
        buyers = new HashMap<>();
        sellers = new HashMap<>();

        HashSet<Integer> buyer_ids = new HashSet<>(buyer_size);
        for (int b = 0; b < buyer_size; b++) {
            buyer_ids.add(b);
        }
        HashSet<Integer> seller_ids = new HashSet<>(seller_size);
        for (int s = 0; s < seller_size; s++) {
            seller_ids.add(s);
        }

        for (int b = 0; b < buyer_size; b++) {
            buyers.put(b, new Buyer(seller_ids));
        }
        for (int s = 0; s < seller_size; s++) {
            sellers.put(s, new Seller(buyer_ids));
        }
    }

    // buyers randomly assigned to sellers
    // if transact, they are done for the day
    // if not, remove leave them in
    public void day() {
        System.out.println("new day");
        Integer[] buyer_id_arr = buyers.keySet().toArray(new Integer[buyer_size]);
        Integer[] seller_id_arr = sellers.keySet().toArray(new Integer[seller_size]);
        List<Integer> buyer_ids = new ArrayList<>(Arrays.asList(buyer_id_arr));
        List<Integer> seller_ids = new ArrayList<>(Arrays.asList(seller_id_arr));
        int round = 0;
        while (!buyer_ids.isEmpty() && !seller_ids.isEmpty()) {
            System.out.println("round "+ round);
            double day_surplus = 0.0;
            ArrayList<Integer> delete_buyers = new ArrayList<>();
            ArrayList<Integer> delete_sellers = new ArrayList<>();
            Collections.shuffle(buyer_ids);
            Collections.shuffle(seller_ids);
            int size;
            if (buyer_ids.size() < seller_ids.size()) {
                size = buyer_ids.size();
            }
            else {
                size = seller_ids.size();
            }
            for (int i = 0; i < size; i++) {
                int buyer_id = buyer_ids.get(i);
                int seller_id = seller_ids.get(i);
                Buyer b = buyers.get(buyer_id);
                Seller s = sellers.get(seller_id);
                double p = s.propose_price();
                System.out.println("buyer " + buyer_id + ", seller " + seller_id);
                System.out.println("buyer expects " + b.expected_price + ", seller expects " +s.expected_price);
                if (Transactor.transact(b,s,p)) {
                    day_surplus += Transactor.transaction_surplus(b,s,p);
                    b.update_expected_price(p);
                    s.update_expected_price(p);
                    delete_buyers.add(buyer_id);
                    delete_sellers.add(seller_id);
                    System.out.println("transaction success");
                }
                else {
                    b.ids.remove(seller_id);
                    if (b.ids.size()<=1) {
                        delete_buyers.add(buyer_id);
                    }
                    s.ids.remove(buyer_id);
                    if (s.ids.size()<=1) {
                        delete_sellers.add(seller_id);
                    }
                    System.out.println("transaction failure");
                }
            }
            for (int j = delete_buyers.size()-1; j>=0; j--) {
                buyer_ids.remove(delete_buyers.get(j));
                for (Map.Entry<Integer, Buyer> entry : buyers.entrySet()) {
                    entry.getValue().ids.remove(delete_buyers.get(j));
                }
            }
            for (int j = delete_sellers.size()-1; j>=0; j--) {
                seller_ids.remove(delete_sellers.get(j));
                for (Map.Entry<Integer, Seller> entry : sellers.entrySet()) {
                    entry.getValue().ids.remove(delete_sellers.get(j));
                }
            }

            System.out.println("today's surplus: " + day_surplus);
            round++;
        }
    }

}
