import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {

        Market m = new Market(2, 2);
        for (int i = 0; i < 50; i++) {
            m.day();
        }
    }

}
