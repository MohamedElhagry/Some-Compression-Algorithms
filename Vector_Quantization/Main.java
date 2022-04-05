import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filePath;
        int codeBookSize;
        int vectorDimension;
        System.out.println("Name : Mohamed Amr Elhagry ");
        System.out.println("ID : 20190725");
        System.out.println("3CS-1");

        System.out.println("Please enter the filepath of the image you want to compress, the size of the codebook , and the dimensions of the vector");
        filePath = sc.nextLine();
        codeBookSize = sc.nextInt();
        vectorDimension = sc.nextInt();

        System.out.println("The compressed data file will be stored on your desktop and so will the output image");

        Compression compression = new Compression(vectorDimension, codeBookSize, filePath);
        Decompression decompression = new Decompression();
        compression.compress();
        decompression.Decompress();
    }

}
