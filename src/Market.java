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
            buyers.put(b, new Buyer(b, new HashSet<>(seller_ids)));
        }
        for (int s = 0; s < seller_size; s++) {
            sellers.put(s, new Seller(s, new HashSet<>(buyer_ids)));
        }
    }

    public void reset() {
        Integer[] buyer_id_arr = buyers.keySet().toArray(new Integer[buyer_size]);
        Integer[] seller_id_arr = sellers.keySet().toArray(new Integer[seller_size]);
        for (Map.Entry<Integer, Buyer> entry : buyers.entrySet()) {
            entry.getValue().reset(new HashSet<>(Arrays.asList(seller_id_arr)));
        }
        for (Map.Entry<Integer, Seller> entry : sellers.entrySet()) {
            entry.getValue().reset(new HashSet<>(Arrays.asList(buyer_id_arr)));
        }
    }

    public void day() {

        System.out.println("new day");
        Integer[] buyer_id_arr = buyers.keySet().toArray(new Integer[buyer_size]);
        Integer[] seller_id_arr = sellers.keySet().toArray(new Integer[seller_size]);
        List<Integer> buyer_ids = new ArrayList<>(Arrays.asList(buyer_id_arr));
        List<Integer> seller_ids = new ArrayList<>(Arrays.asList(seller_id_arr));
        int round = 0;

        while (!buyer_ids.isEmpty() && !seller_ids.isEmpty()) {
            System.out.println("round " + round);

            HashSet<Integer> delete_buyers = new HashSet();
            HashSet<Integer> delete_sellers = new HashSet();
            HashSet<Integer> seen_sellers = new HashSet();
            Collections.shuffle(buyer_ids);
            Collections.shuffle(seller_ids);

            // transact
            for (int i : buyer_ids) {
                Buyer b = buyers.get(i);
                Seller s = null;
                for (int j : seller_ids) {
                    if (!seen_sellers.contains(sellers.get(j).id) &&
                            !delete_sellers.contains(sellers.get(j).id) &&
                            b.ids.contains(sellers.get(j).id)) {
                        s = sellers.get(j);
                        seen_sellers.add(s.id);
                        break;
                    }
                }
                if (s != null) {
                    double p = Transactor.propose_price(s);
                    if (Transactor.transact(b,s,p)) {
                        delete_buyers.add(b.id);
                        delete_sellers.add(s.id);
                        b.update_expected_price(p, true);
                        s.update_expected_price(p, true);
                        System.out.println("SUCCESS: " +
                                "buyer " + b.id + " " + (int)b.bound + "/" + (int)b.expected_price +
                                ", seller " + s.id + " " + (int)s.bound + "/" + (int)s.expected_price +
                                ", price " + (int)p);
                    }
                    else {
                        System.out.println("FAILURE: " +
                                "buyer " + b.id + " " + (int)b.bound + "/" + (int)b.expected_price +
                                ", seller " + s.id + " " + (int)s.bound + "/" + (int)s.expected_price +
                                ", price " + (int)p);
                    }
                }
            }

            // clean-up
            for (int i : buyer_ids) {
                if (!delete_buyers.contains(i)) {
                    Buyer b = buyers.get(i);
                    boolean flag = true;
                    for (int j : seller_ids) {
                        if (b.ids.contains(sellers.get(j))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        boolean satisfied = b.update_status(
                                new HashSet(Arrays.asList(seller_id_arr))
                        );
                        if (satisfied) {
                            delete_buyers.add(b.id);
                            b.update_expected_price(-1, false);
                        }
                    }
                }
            }
            for (int i : seller_ids) {
                if (!delete_sellers.contains(i)) {
                    Seller s = sellers.get(i);
                    boolean flag = true;
                    for (int j : buyer_ids) {
                        if (s.ids.contains(buyers.get(j))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        boolean satisfied = s.update_status(
                                new HashSet(Arrays.asList(buyer_id_arr))
                        );
                        if (satisfied) {
                            delete_sellers.add(s.id);
                            s.update_expected_price(-1, false);
                        }
                    }
                }
            }

            // delete
            for (Integer b : delete_buyers) {
                buyer_ids.remove(b);
            }
            for (Integer s : delete_sellers) {
                seller_ids.remove(s);
            }
            round ++;
        }
        reset();
    }
}
