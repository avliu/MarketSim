public class Main {

    public static void main(String[] args) {
        Market m = Market.even();
        m.print_info();
        for (int i = 0; i < 50; i++) {
            m.day();
        }
    }

}
