import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Decompression {
    int dim;
    int codeBookSize;
    String inFilepath;
    String imageFilePath;
    int associations[];
    ArrayList<int[][]> codebook;
    int[][] pixels;
    int width;
    int height;

    Decompression() {
        inFilepath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "CompressedData.txt";
        this.imageFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "image.png";
        ;
        codebook = new ArrayList<int[][]>();

    }

    int binaryToInt(String b) {
        int m = 1;
        int c = 0;
        for (int i = b.length() - 1; i >= 0; i--) {
            c += (b.charAt(i) - '0') * m;
            m *= 2;
        }
        return c;
    }

    public void toImage(String s) {
        BufferedImage img = new BufferedImage(
                pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                img.setRGB(x, y, new Color((int) pixels[x][y], (int) pixels[x][y], (int) pixels[x][y]).getRGB());
            }
        }
        File imageFile = new File(s);
        try {
            ImageIO.write(img, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFromFile() {
        StringBuilder compressedStrInput = new StringBuilder();
        try {
            FileInputStream in = new FileInputStream(inFilepath);

            int c;
            while ((c = in.read()) != -1) {
                compressedStrInput.append((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String compressedData = compressedStrInput.toString();
        int ptr = 0;

        StringBuilder temp = new StringBuilder();
        while (ptr != 32) {
            temp.append(compressedData.charAt(ptr));
            ptr++;
        }
        width = binaryToInt(temp.toString());

        temp = new StringBuilder();
        while (ptr != 64) {
            temp.append(compressedData.charAt(ptr));
            ptr++;
        }
        height = binaryToInt(temp.toString());

        temp = new StringBuilder();
        while (ptr != 96) {
            temp.append(compressedData.charAt(ptr));
            ptr++;
        }
        codeBookSize = binaryToInt(temp.toString());

        temp = new StringBuilder();
        while (ptr != 128) {
            temp.append(compressedData.charAt(ptr));
            ptr++;
        }
        dim = binaryToInt(temp.toString());


        for (int i = 0; i < codeBookSize; i++) {
            int[][] vec = new int[dim][dim];
            for (int x = 0; x < dim; x++) {
                for (int y = 0; y < dim; y++) {

                    temp = new StringBuilder();
                    for (int c = 0; c < 8; c++) {
                        temp.append(compressedData.charAt(ptr));
                        ptr++;
                    }
                    vec[x][y] = binaryToInt(temp.toString());
                }
            }
            codebook.add(vec);
        }

        associations = new int[width * height / (dim * dim)];

        for (int i = 0; i < associations.length; i++) {
            temp = new StringBuilder();
            for (int c = 0; c < 16; c++) {
                temp.append(compressedData.charAt(ptr));
                ptr++;
            }
            associations[i] = binaryToInt(temp.toString());
        }

        /*
        System.out.println(width);
        System.out.println(height);
        System.out.println(codeBookSize);
        System.out.println(dim);

        for (int i = 0; i < associations.length; i++) {
            System.out.print(associations[i] + " ");
        }

        for (int i = 0; i < codeBookSize; i++) {
            int[][] vec = codebook.get(i);
            for (int j = 0; j < vec.length; j++) {
                for (int k = 0; k < vec.length; k++) {
                    System.out.print(vec[j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        */

    }

    void Decompress() {
        readFromFile();

        pixels = new int[width][height];
        int row = 0;
        int col = 0;
        for (int i = 0; i < associations.length; i++) {
            int[][] vec = codebook.get(associations[i]);

            for (int x = 0; x < dim; x++) {
                for (int y = 0; y < dim; y++) {
                    pixels[row + x][col + y] = vec[x][y];
                }
            }
            col += dim;
            if (col == height) {
                row += dim;
                col = 0;
            }
        }

        toImage(imageFilePath);
    }

    public static void main(String[] args) {
        Decompression decompression = new Decompression();
        decompression.Decompress();
    }


}
