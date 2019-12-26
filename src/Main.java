import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Main {



    public static void main(String[] args) {
        Market m = Market.less_buyers();
        m.print_bounds();
        for (int i = 0; i < 1000; i++) {
            m.day();
        }
    }

}
