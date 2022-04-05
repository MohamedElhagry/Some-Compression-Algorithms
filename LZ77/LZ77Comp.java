import java.util.ArrayList;

public class LZ77Comp {

    int getBits(int num) {
        int bits = 0;
        while (num > 0) {
            num /= 2;
            bits++;
        }
        return bits;
    }

    private String numToBinary(int num) {
        // numbers are written from left to right
        String s = "";
        while (num > 0) {
            s = s + Integer.toString(num % 2);
            num /= 2;
        }
        return s;
    }

    ArrayList<LZ77Tag> compressedData;
    int posSize, lenSize;
    String CompressedStr;
    int searchWindowSize, lookAheadWindowSize;

    LZ77Comp(int searchWindowSize, int lookAheadWindowSize) {
        compressedData = new ArrayList<LZ77Tag>();
        posSize = 0;
        lenSize = 0;
        CompressedStr = "";
        this.searchWindowSize = searchWindowSize;
        this.lookAheadWindowSize = lookAheadWindowSize;
    }

    public String compress(String Data) {
        for (int c = 0; c < Data.length(); c++) {
            LZ77Tag temp = new LZ77Tag(0, 0, Data.charAt(c));

            //looping through all previous strings in search window to find
            //best match
            for (int pos = 1; pos <= c && pos <= searchWindowSize; pos++) {
                int len = 0;
                while (c + len < Data.length() && len < lookAheadWindowSize &&
                        Data.charAt(c - pos + len) == Data.charAt(c + len)) {
                    len++;
                }

                // deciding whether to replace currentTag
                if (len > temp.len) {
                    //if end of data
                    if (c + len == Data.length()) temp = new LZ77Tag(pos, len, '0');
                    else temp = new LZ77Tag(pos, len, Data.charAt(c + len));
                }
            }
            compressedData.add(temp);
            c = c + temp.len;
        }

        //decide max size of pos and len
        posSize = 0;
        lenSize = 0;
        for (int i = 0; i < compressedData.size(); i++) {
            //System.out.println( compressedData.get(i).pos + " " + compressedData.get(i).len + " " + compressedData.get(i).nextSymbol);
            posSize = Math.max(posSize, getBits(compressedData.get(i).pos));
            lenSize = Math.max(lenSize, getBits(compressedData.get(i).len));
        }

        CompressedStr = "";
        //transform tags into binary string
        for (int i = 0; i < compressedData.size(); i++) {
            CompressedStr += compressedData.get(i).ConvertToBinary(posSize, lenSize);
        }

        return CompressedStr;
    }

    public void viewTags() {
        for (int i = 0; i < compressedData.size(); i++)
            System.out.println(compressedData.get(i).pos + " " + compressedData.get(i).len + " " + compressedData.get(i).nextSymbol);
    }

    public void compressionRatio(String Data) {
        String unCompressedString = "";
        for (int i = 0; i < Data.length(); i++)
            unCompressedString += numToBinary(Data.charAt(i));

        System.out.println("Compression Ratio (Original : Compressed) = " + unCompressedString.length() + " : " + CompressedStr.length());
    }

    public String strToBinary(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++)
            res += numToBinary(s.charAt(i));
        return res;
    }


    public static void main(String[] args) {

        //Demo
        System.out.println("Student Name: Mohamed Amr Elhagry , ID = 20190725");
        String data = "ABAABABAABBBBBBBBBBBBA";

        System.out.println("Original data: " + data);

        LZ77Comp obj = new LZ77Comp(7, 10);
        obj.compress(data);
        System.out.println("Tags resulting from compressing the data: ");
        obj.viewTags();
//        System.out.println("Position Size: " + obj.posSize + ", Length Size: " + obj.lenSize);
        String compressedStr = obj.CompressedStr;
        System.out.println("Original data (as a binary string): " + obj.strToBinary(data));
        System.out.println("Compressed data (as a binary string): " + compressedStr);
        obj.compressionRatio(data);

        //notice that we do not pass the actual tags to the decompression class, we only pass a binary string
        LZ77Decomp obj2 = new LZ77Decomp(obj.posSize, obj.lenSize);
        String decompressedStr = obj2.decompress(compressedStr);
        System.out.println("Compressed data after begin decompressed: " + decompressedStr);
    }


}
