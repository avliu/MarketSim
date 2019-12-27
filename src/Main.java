public class Main {

    public static void main(String[] args) {
        int[] buyer_bounds = {100,90,80,70,60,50,40,30,20,10};
        int[] buyer_exp = {90,80,70,60,50,40,30,20,10,5};
        int[] seller_bounds = {10,20,30,40,50,60,70,80,90,95};
        int[] seller_exp = {20,30,40,50,60,70,80,90,95,100};

//        int[] buyer_bounds = {90};
//        int[] buyer_exp = {70};
//        int[] seller_bounds = {10,20};
//        int[] seller_exp = {30,40};

//        int[] buyer_bounds = {90,80};
//        int[] buyer_exp = {70,60};
//        int[] seller_bounds = {10};
//        int[] seller_exp = {30};

        Market m = new Market(buyer_bounds, seller_bounds, buyer_exp, seller_exp);
        m.print_info();
        for (int i = 0; i < 50; i++) {
            System.out.println("day " + i);
            m.day();
        }
    }

}
