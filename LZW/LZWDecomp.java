import java.util.ArrayList;

public class LZWDecomp {

    int posSize;

    int binaryToNum(String b) {
        int m = 1;
        int temp = 0;
        for (int i = 0; i < b.length(); i++) {
            temp += (b.charAt(i) - '0') * m;
            m *= 2;
        }
        return temp;
    }

    LZWDecomp(int posSize) {
        this.posSize = posSize;
    }

    String decompress(String compressedStr) {
        ArrayList<Integer> tags = new ArrayList<Integer>();

        int ptr = 0;
        while (ptr < compressedStr.length()) {
            int num = binaryToNum(compressedStr.substring(ptr, ptr + posSize));
            tags.add(num);
            ptr += posSize;
        }

        String data = "";
        LZWDictionary dict = new LZWDictionary();
        String prev = "";
        for (int i = 0; i < tags.size(); i++) {
            String s = "";
            if (tags.get(i) == dict.getSize()) {
                s = prev + prev.charAt(0);
                dict.add(s);
                data += s;
                prev = s;
            } else {
                s = dict.get(tags.get(i));
                data += s;
                if (i != 0)
                    dict.add(prev + s.charAt(0));
                prev = s;
            }

        }

        return data;
    }
}
