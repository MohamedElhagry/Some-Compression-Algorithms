import java.util.ArrayList;

public class LZ77Decomp {

    int binaryToNum(String b) {
        int m = 1;
        int temp = 0;
        for (int i = 0; i < b.length(); i++) {
            temp += (b.charAt(i) - '0') * m;
            m *= 2;
        }
        return temp;
    }

    int posSize, lenSize;

    LZ77Decomp(int posSize, int lenSize) {
        this.posSize = posSize;
        this.lenSize = lenSize;
    }

    public String decompress(String compressedStrInput) {
        ArrayList<LZ77Tag> tags = new ArrayList<LZ77Tag>();

        //populate the tags array
        int ptr = 0;
        while (ptr < compressedStrInput.length()) {
            int currPos, currLen, currChar;
            currPos = binaryToNum(compressedStrInput.substring(ptr, ptr + posSize));
            ptr += posSize;
            currLen = binaryToNum(compressedStrInput.substring(ptr, ptr + lenSize));
            ptr += lenSize;
            currChar = binaryToNum(compressedStrInput.substring(ptr, ptr + 7));
            ptr += 7;
            tags.add(new LZ77Tag(currPos, currLen, (char) currChar));

        }

//        for (int i = 0; i < tags.size(); i++) {
//            System.out.println(compressedData.get(i).pos + " " + compressedData.get(i).len + " " + compressedData.get(i).nextSymbol);
//        }

        String recoveredData = "";
        ptr = 0;

        for (int i = 0; i < tags.size(); i++) {
            LZ77Tag curr = tags.get(i);

            if (curr.pos != 0)
                for (int j = 0; j < curr.len; j++) {
                    recoveredData += recoveredData.charAt(ptr - curr.pos);
                    ptr++;
                }
            if (curr.nextSymbol != '0') {
                recoveredData += curr.nextSymbol;
                ptr++;
            }
        }

        return recoveredData;
    }
}
