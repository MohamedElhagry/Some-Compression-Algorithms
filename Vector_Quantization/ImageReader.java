import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    public String toBinary(int c) {
        String s = "";
        while (!(s.length() == 32)) {
            if (c % 2 == 0) s = '0' + s;
            else s = '1' + s;
            c /= 2;
        }
        return s;
    }

    public int[][] readImage(String filepath, int dimension) {
        File file = new File(filepath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int w = image.getWidth();
        int h = image.getHeight();

        int scaledWidth = w, scaledHeight = h;
        while (scaledHeight % dimension != 0) scaledHeight++;
        while (scaledWidth % dimension != 0) scaledWidth++;
        int[][] pixels = new int[scaledWidth][scaledHeight];


        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int color = image.getRGB(x, y);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                pixels[x][y] = (int) (red * 0.299 + green * 0.587 + blue * 0.114);
            }
        }

        return pixels;
    }

    public static void main(String[] args) {
        ImageReader imageReader = new ImageReader();
        imageReader.readImage("G:\\College\\Year 3\\Fall\\Information Theory and Data Compression\\index.jpg", 2);

    }

}
