import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Decompression_20190725 {

    String inFilePath; // reads the compressed data
    String outFilePath; // outputs the decompressed data

    char binaryToChar(String b) {
        int m = 1;
        int c = 0;
        for (int i = b.length() - 1; i >= 0; i--) {
            c += (b.charAt(i) - '0') * m;
            m *= 2;
        }
        return (char) c;
    }


    Decompression_20190725() {
        this.inFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "CompressedData.txt";
        this.outFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "DecompressedData.txt";
    }

    void writeToFile(String line) {
        try {
            FileOutputStream out = new FileOutputStream(outFilePath);

            for (int i = 0; i < line.length(); i++) {
                out.write(line.charAt(i));
            }

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String decompress() {
        TreeMap<String, Character> dict = new TreeMap<String, Character>();
        try {
            FileInputStream in = new FileInputStream(inFilePath);

            int c;
            StringBuilder currChar = new StringBuilder();
            StringBuilder code = new StringBuilder();
            byte[] buffer = new byte[7];
            boolean end = false;

            int cntr = 0;
            while (!end && (in.read(buffer)) != -1) {
                for (int i = 0; i < 7; i++) currChar.append((char) buffer[i]);
                cntr += 7;
                while ((c = in.read()) != -1) {
                    cntr++;
                    if (c == ',' || c == '.') {
                        dict.put(code.toString(), binaryToChar(currChar.toString()));
                        code = new StringBuilder();
                        currChar = new StringBuilder();
                        if (c == '.') end = true;
                        break;
                    }
                    code.append((char) c);
                }
            }

            System.out.println("The dictionary retrieved from the compressed data:");

            for (Map.Entry<String, Character> e : dict.entrySet()) {
                System.out.println(e.getKey() + " " + e.getValue());
            }

            StringBuilder currCode = new StringBuilder("");
            StringBuilder DecompressedData = new StringBuilder("");
            while ((c = in.read()) != -1) {
                currCode.append((char) c);
                if (dict.containsKey(currCode.toString())) {
                    DecompressedData.append(dict.get(currCode.toString()));
                    currCode = new StringBuilder("");
                }
            }

            writeToFile(DecompressedData.toString());
            System.out.println("Compressed bit size = " + cntr);
            System.out.println("Decompressed String : " + DecompressedData.toString());

            return DecompressedData.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        Decompression_20190725 d = new Decompression_20190725();

        System.out.println("DecompressedData: ");
        System.out.println(d.decompress());

    }

}
