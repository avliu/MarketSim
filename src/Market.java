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

    public Market(int[] buyer_bound_arr, int[] seller_bound_arr,
                  int[] buyer_exp_arr, int[] seller_exp_arr) {
        buyer_size = buyer_bound_arr.length;
        seller_size = seller_bound_arr.length;
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
            buyers.put(b, new Buyer(b, new HashSet<>(seller_ids), buyer_bound_arr[b], buyer_exp_arr[b]));
        }
        for (int s = 0; s < seller_size; s++) {
            sellers.put(s, new Seller(s, new HashSet<>(buyer_ids), seller_bound_arr[s], seller_exp_arr[s]));
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

    public void print_report(double b_surplus, double s_surplus) {
        String b = "BUYER SURPLUS: " + b_surplus + ", EXPECTATIONS:  ";
        for (int i = 0; i < buyer_size; i++) {
            b += (i + ":" + (int)buyers.get(i).expected_price + " ");
        }
        System.out.println(b);
        String s = "SELLER SURPLUS: " + s_surplus + ", EXPECTATIONS: ";
        for (int i = 0; i < seller_size; i++) {
            s += (i + ":" + (int)sellers.get(i).expected_price + " ");
        }
        System.out.println(s);
        System.out.println();
    }

    public void print_info() {
        System.out.println("(format is id:bound/expected price)");
        String b = "BUYER INFO:  ";
        for (int i = 0; i < buyer_size; i++) {
            b += (i + ":" + (int)buyers.get(i).bound + "/" + (int)buyers.get(i).expected_price + " ");
        }
        System.out.println(b);
        String s = "SELLERS INFO: ";
        for (int i = 0; i < seller_size; i++) {
            s += (i + ":" + (int)sellers.get(i).bound + "/" + (int)sellers.get(i).expected_price + " ");
        }
        System.out.println(s);
        System.out.println();
    }

    public void day() {

        Integer[] buyer_id_arr = buyers.keySet().toArray(new Integer[buyer_size]);
        Integer[] seller_id_arr = sellers.keySet().toArray(new Integer[seller_size]);
        List<Integer> buyer_ids = new ArrayList<>(Arrays.asList(buyer_id_arr));
        List<Integer> seller_ids = new ArrayList<>(Arrays.asList(seller_id_arr));
        double buyer_surplus = 0;
        double seller_surplus = 0;

        while (!buyer_ids.isEmpty() && !seller_ids.isEmpty()) {
//            System.out.println("    round " + round);

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
                    double p = Transactor.transact(b,s);
                    if (p >= 0) {
                        delete_buyers.add(b.id);
                        delete_sellers.add(s.id);
                        System.out.println("TRANSACTION: " +
                                "buyer #" + b.id +
                                ", seller #" + s.id +
                                ", price " + (int)p);
                        b.update_expected_price(true);
                        s.update_expected_price(true);
                        buyer_surplus += (b.bound - p);
                        seller_surplus += (p - s.bound);
                    }
                }
            }

            // clean-up
            for (int i : buyer_ids) {
                if (!delete_buyers.contains(i)) {
                    Buyer b = buyers.get(i);
                    boolean flag = true;
                    for (int j : seller_ids) {
                        if (b.ids.contains(j)) {
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
                            b.update_expected_price(false);
                        }
                    }
                }
            }
            for (int i : seller_ids) {
                if (!delete_sellers.contains(i)) {
                    Seller s = sellers.get(i);
                    boolean flag = true;
                    for (int j : buyer_ids) {
                        if (s.ids.contains(j)) {
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
                            s.update_expected_price(false);
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

//            System.out.println("Remaining buyers: " + buyer_ids);
//            System.out.println("Remaining sellers: " + seller_ids);

        }

        // final cleanup for those still remaining in buyer_ids or seller_ids
        for (Integer b : buyer_ids) {
            buyers.get(b).update_expected_price(false);
        }
        for (Integer s : seller_ids) {
            sellers.get(s).update_expected_price(false);
        }

        print_report(buyer_surplus, seller_surplus);
        reset();
    }

}
