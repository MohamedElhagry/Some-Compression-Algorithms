import java.util.ArrayList;

public class LZWDictionary {

    ArrayList<String> dict;

    LZWDictionary() {
        dict = new ArrayList<String>();
        String temp = "";
        for (int i = 0; i < 128; i++) {
            temp = "";
            temp += (char) i;
            dict.add(temp);
        }
    }

    String get(int i) {
        return dict.get(i);
    }

    int getSize() {
        return dict.size();
    }

    void add(String s) {
        dict.add(s);
    }

    int find(String s) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals(s)) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
//
//        Dictionary dict = new Dictionary();
//
//        for (int i = 0; i < 128; i++) {
//            System.out.println(dict.get(i));
//        }
    }
}
