import java.io.*;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Compression_20190725 {

    String inFilePath; // read the original line
    String outFilePath; // outputs the compressed data

    String toBinary(int c) {
        String s = "";
        while (c > 0) {
            s = Integer.toString(c % 2) + s;
            c /= 2;
        }
        while (!(s.length() == 7)) s = "0" + s;
        return s;
    }

    Compression_20190725() {
        this.inFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "Original.txt";
        this.outFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "CompressedData.txt";
    }

    String readFromFile() {
        File f = new File(inFilePath);
        if (!f.exists()) System.out.println("There is no file named 'Original.txt' in your desktop");

        StringBuilder line = new StringBuilder();
        try {
            FileInputStream in = new FileInputStream(inFilePath);
            int c;
            while ((c = in.read()) != -1) {
                line.append((char) c);
            }

            return line.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


    void writeDictToFile(TreeMap<Character, String> dict) {
        try {
            //System.out.println(filePath);
            FileOutputStream out = new FileOutputStream(outFilePath);
            for (Map.Entry<Character, String> e : dict.entrySet()) {
                out.write(toBinary(e.getKey()).getBytes());
                out.write(e.getValue().getBytes());
                if (e.getKey() == dict.lastEntry().getKey())
                    out.write(".".getBytes());
                else
                    out.write(",".getBytes());
            }
//            out.write('\n');

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeDataToFile(String compressedStr) {
        try {
            //System.out.println(filePath);
            FileOutputStream out = new FileOutputStream(outFilePath, true);
            out.write(compressedStr.getBytes());
//            out.write('\n');

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String compress() {
        String line = readFromFile();

        //count the number of occurences of each character
        TreeMap<Character, Integer> map = new TreeMap<Character, Integer>();
        System.out.println("Original sentence bit size =  " + line.length() * 7);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (map.containsKey(c)) map.put(c, map.get(c) + 1);
            else
                map.put(c, 1);
        }


        //do huffman encoding
        System.out.println("The count of characters : ");
        PriorityQueue<Node_20190725> pq = new PriorityQueue<Node_20190725>();
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            pq.add(new Node_20190725(e.getKey().toString(), e.getValue()));
            System.out.println(e.getKey() + " : " + e.getValue());
        }
        System.out.println();


        //construct the dictionary
        TreeMap<Character, String> dict = new TreeMap<Character, String>();
        while (pq.size() > 1) {

            Node_20190725 smaller = pq.remove();
            Node_20190725 bigger = pq.remove();
            String smallerL = smaller.letters, biggerL = bigger.letters;

            for (int i = 0; i < smallerL.length(); i++) {
                char c = smallerL.charAt(i);
                if (dict.containsKey(c)) {
                    dict.put(c, '1' + dict.get(c));
                } else
                    dict.put(c, "1");
            }
            for (int i = 0; i < biggerL.length(); i++) {
                char c = biggerL.charAt(i);
                if (dict.containsKey(c)) {
                    dict.put(c, '0' + dict.get(c));
                } else
                    dict.put(c, "0");
            }

            pq.add(new Node_20190725(smallerL + biggerL, smaller.p + bigger.p));
        }

        //write the dictionary to the file
        writeDictToFile(dict);

        for (Map.Entry<Character, String> e : dict.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
        System.out.println();


        //construct the compressed string
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) stringBuilder.append(dict.get(line.charAt(i)));
        String compressedStr = stringBuilder.toString();


        writeDataToFile(compressedStr);
        return compressedStr;
    }


    public static void main(String[] args) {
        Compression_20190725 comp = new Compression_20190725();
        comp.compress();

    }


}
