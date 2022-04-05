import java.io.File;
import java.io.IOException;

public class Main_20190725 {

    public static void main(String[] args) {

        System.out.println("Mohamed Amr Elhagry , 20190725 , 3CS-S1");

        File original = new File(System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "Original.txt");
        File compressed = new File(System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "CompressedData.txt");
        File decompressed = new File(System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "DecompressedData.txt");
        try {
            original.createNewFile();
            compressed.createNewFile();
            decompressed.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("This program creates three files in your desktop ,\"Original.txt\", \"CompressedData.txt\", and \"DecompressedData.txt\"");
        System.out.println("Please store the line you want to compress in \"Original.txt\", the comrpessed output will be stored in \"CompressedData.txt\"");
        System.out.println("That compressed output is then decompressed then written to  \"DecompressedData.txt\"");

        System.out.println("Write something to \"Original.txt\" and then run the program again for it to compress something");

        Compression_20190725 comp = new Compression_20190725();
        Decompression_20190725 decomp = new Decompression_20190725();

        comp.compress();
        decomp.decompress();
    }

}
