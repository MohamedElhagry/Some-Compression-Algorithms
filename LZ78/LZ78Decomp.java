import java.util.ArrayList;

public class LZ78Decomp {

    int binaryToNum(String b) {
        int m = 1;
        int temp = 0;
        for (int i = 0; i < b.length(); i++) {
            temp += (b.charAt(i) - '0') * m;
            m *= 2;
        }
        return temp;
    }

    int indSize;

    LZ78Decomp(int indSize) {
        this.indSize = indSize;
    }

    String decompress(String compressedString) {
        ArrayList<LZ78Tag> tags = new ArrayList<LZ78Tag>();

        int ptr = 0;
        while (ptr < compressedString.length()) {
            int ind, currChar;
            ind = binaryToNum(compressedString.substring(ptr, ptr + indSize));
            ptr += indSize;
            currChar = binaryToNum(compressedString.substring(ptr, ptr + 7));
            ptr += 7;

            tags.add(new LZ78Tag(ind, (char) currChar));
        }

        LZ78Dictionary dict = new LZ78Dictionary();

        String data = ""; // recovered Data
        for (int i = 0; i < tags.size(); i++) {
            LZ78Tag curr = tags.get(i);
            String temp = dict.get(curr.index);
            temp += curr.nextSymbol;
            dict.add(temp);

            data += temp;
        }

        return data;

    }


}
