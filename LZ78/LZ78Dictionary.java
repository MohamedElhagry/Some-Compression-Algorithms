import java.util.ArrayList;

public class LZ78Dictionary {

    ArrayList<String> dict;

    LZ78Dictionary() {
        dict = new ArrayList<String>();
        dict.add("");
    }

    String get(int i) {
        return dict.get(i);
    }

    void add(String s) {
        dict.add(s);
    }

    int find(String s) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals(s)) return i;
        }
        return 0;
    }
}
