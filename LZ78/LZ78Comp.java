import java.util.ArrayList;

public class LZ78Comp {

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

    ArrayList<LZ78Tag> tags;
    int indexSize;

    LZ78Comp() {
        indexSize = 0;
        tags = new ArrayList<LZ78Tag>();
    }

    public String compress(String data) {

        String temp = "";
        int lastMatch = 0;
        LZ78Dictionary dict = new LZ78Dictionary();
        tags = new ArrayList<LZ78Tag>();

        for (int i = 0; i < data.length(); i++) {
            temp += data.charAt(i);

            int ind = dict.find(temp);
            if (ind == 0 || i == data.length() - 1) {
                tags.add(new LZ78Tag(lastMatch, data.charAt(i)));
                dict.add(temp);
                lastMatch = 0;
                temp = "";
            } else {
                lastMatch = ind;
            }
        }

        String binaryString = "";

        indexSize = 0;
        for (int i = 0; i < tags.size(); i++) {
            indexSize = Math.max(indexSize, getBits(tags.get(i).index));
        }


        for (int i = 0; i < tags.size(); i++) {
            binaryString += tags.get(i).ConvertToBinary(indexSize);
        }

        return binaryString;
    }

    public void showTags() {
        LZ78Tag temp;
        for (int i = 0; i < tags.size(); i++) {
            temp = tags.get(i);
            System.out.println(temp.index + " " + temp.nextSymbol);
        }

    }

    public void compressionRatio(String data) {
        String unCompressedStr = "";
        for (int i = 0; i < data.length(); i++)
            unCompressedStr += numToBinary(data.charAt(i));
        System.out.println("Compression Ratio (Original: Compressed) = " + unCompressedStr.length() + " : " + compress(data).length());
    }

    public static void main(String[] args) {
        System.out.println("Student Name: Mohamed Amr Elhagry , ID = 20190725");
        String data = "ABAABABAABABBBBBBBBBBA";

        LZ78Comp obj = new LZ78Comp();
        String compressedString = obj.compress(data);
        System.out.println("Compressed Binary String: " + compressedString);
        obj.showTags();
        obj.compressionRatio(data);


        //note that we dont pass the tags to the decompression class, we only pass the compressed string itself
        LZ78Decomp obj2 = new LZ78Decomp(obj.indexSize);
        String decompressedStr = obj2.decompress(compressedString);
        System.out.println("Decompressed String : " + decompressedStr);
    }

}
