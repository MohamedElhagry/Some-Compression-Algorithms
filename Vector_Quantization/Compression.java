import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Compression {
    int dim;
    int codeBookSize;
    ImageReader imageReader;
    ArrayList<int[][]> averages;
    int[] associations;
    String filepath;
    String outFilePath;

    int[][] pixels;


    Compression(int dim, int codeBookSize, String filePath) {
        this.dim = dim;
        this.codeBookSize = codeBookSize;
        optimizeCodeBookSize();

        imageReader = new ImageReader();
        this.filepath = filePath;
        this.outFilePath = System.getProperty("user.home") + File.separatorChar + "Desktop" + File.separatorChar + "CompressedData.txt";
    }

    void optimizeCodeBookSize() {
        while (Integer.bitCount(codeBookSize) != 1) {
            codeBookSize++;
        }
    }

    String toBinary(int c, int len) {
        String s = "";
        while (c > 0) {
            s = Integer.toString(c % 2) + s;
            c /= 2;
        }
        while (!(s.length() == len)) s = "0" + s;
        return s;
    }

    void writeToFile() {
        try {
            //System.out.println(filePath);
            FileOutputStream out = new FileOutputStream(outFilePath, false);
            out.write(toBinary(pixels.length, 32).getBytes());
            out.write(toBinary(pixels[0].length, 32).getBytes());
            out.write(toBinary(codeBookSize, 32).getBytes());
            out.write(toBinary(dim, 32).getBytes());

            for (int i = 0; i < averages.size(); i++) {
                int[][] vec = averages.get(i);
                for (int x = 0; x < dim; x++) {
                    for (int y = 0; y < dim; y++) {
                        out.write(toBinary(vec[x][y], 8).getBytes());
                    }
                }
            }

            for (int i = 0; i < associations.length; i++) {
                out.write(toBinary(associations[i], 16).getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    ArrayList<int[][]> partition() {
        ArrayList<int[][]> vectors = new ArrayList<int[][]>();


        int w = pixels.length;
        int h = pixels[0].length;
        for (int i = 0; i < w; i += dim) {
            for (int j = 0; j < h; j += dim) {

                int vec[][] = new int[dim][dim];
                for (int k = 0; k < dim; k++) {
                    for (int z = 0; z < dim; z++) {
                        vec[k][z] = pixels[i + k][j + z];
                    }
                }
                vectors.add(vec);
            }
        }

//        for (int i = 0; i < vectors.size(); i++) {
//            for (int x = 0; x < dim; x++) {
//                for (int y = 0; y < dim; y++)
//                    System.out.print(vectors.get(i)[x][y] + " ");
//                System.out.println("");
//            }
//            System.out.println();
//        }

        return vectors;
    }

    int[][] getAverage(ArrayList<int[][]> vectors) {

        int[][] average = new int[dim][dim];
        for (int[][] vec : vectors) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    average[i][j] += vec[i][j];
                }
            }
        }

        if (vectors.size() == 0) return average;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                average[i][j] /= vectors.size();
                //  System.out.print(average[i][j] + " ");
            }
            //System.out.println();
        }
        return average;
    }

    void perturb(int[][] splitVec, int[][] leftVec, int[][] rightVec) {
        for (int i = 0; i < splitVec.length; i++) {
            for (int j = 0; j < splitVec[0].length; j++) {
                leftVec[i][j] = splitVec[i][j];
                rightVec[i][j] = splitVec[i][j] + 1;
            }
        }
    }

    int getDistance(int[][] a, int[][] b) {
        int sqError = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                int error = Math.abs(b[i][j] - a[i][j]);
                sqError += error * error;
            }
        }
        int dimensions = a.length * a[0].length;

        return sqError / dimensions;
    }

    void assign(ArrayList<int[][]> vectors, int[] associations) {

        for (int i = 0; i < vectors.size(); i++) {
            int mnInd = 0;
            int mnError = (int) 1e9;
            for (int j = 0; j < averages.size(); j++) {
                int currError = getDistance(vectors.get(i), averages.get(j));
                if (currError < mnError) {
                    mnError = currError;
                    mnInd = j;
                }
            }
            associations[i] = mnInd;
        }
    }


    void compress() {
        //pixels = new int[][]{{1, 2, 7, 9, 4, 11}, {3, 4, 6, 6, 12, 12}, {4, 9, 15, 14, 9, 9},
        //       {10, 10, 20, 18, 8, 8}, {4, 3, 17, 16, 1, 4}, {4, 5, 18, 18, 5, 6}};

        pixels = imageReader.readImage(filepath, dim);
        ArrayList<int[][]> allVectors = partition();
        associations = new int[allVectors.size()];

        averages = new ArrayList<int[][]>();
        averages.add(getAverage(allVectors));
        for (int i = 0; i < associations.length; i++) associations[i] = 0;


        int cntr = 0;
        while (true) {
            if (averages.size() < codeBookSize) {
                ArrayList<int[][]> newAverages = new ArrayList<int[][]>(averages.size());
                ArrayList<ArrayList<int[][]>> averageOfAssociated = new ArrayList<ArrayList<int[][]>>(averages.size());

                for (int i = 0; i < averages.size(); i++) {
                    averageOfAssociated.add(new ArrayList<int[][]>());
                }

                for (int i = 0; i < associations.length; i++) {
                    ArrayList<int[][]> currAverage = averageOfAssociated.get(associations[i]);
                    currAverage.add(allVectors.get(i));
                    averageOfAssociated.set(associations[i], currAverage);
                }


                for (int i = 0; i < averageOfAssociated.size(); i++) {
                    ArrayList<int[][]> currentVecs = averageOfAssociated.get(i);
                    int[][] currAverage = getAverage(currentVecs);
                    newAverages.add(i, currAverage);
                }

                averages = newAverages;

                newAverages = new ArrayList<int[][]>(averages.size());
                for (int i = 0; i < averages.size(); i++) {
                    int[][] leftVec = new int[dim][dim];
                    int[][] rightVec = new int[dim][dim];
                    perturb(averages.get(i), leftVec, rightVec);
                    newAverages.add(leftVec);
                    newAverages.add(rightVec);
                }

                averages = newAverages;
                assign(allVectors, associations);
            } else if (cntr < 15) {
                cntr++;
                ArrayList<int[][]> newAverages = new ArrayList<int[][]>(averages.size());
                ArrayList<ArrayList<int[][]>> averageOfAssociated = new ArrayList<ArrayList<int[][]>>(averages.size());

                for (int i = 0; i < averages.size(); i++) {
                    averageOfAssociated.add(new ArrayList<int[][]>());
                }

                for (int i = 0; i < associations.length; i++) {
                    ArrayList<int[][]> currAverage = averageOfAssociated.get(associations[i]);
                    currAverage.add(allVectors.get(i));
                    averageOfAssociated.set(associations[i], currAverage);
                }


                for (int i = 0; i < averageOfAssociated.size(); i++) {
                    ArrayList<int[][]> currentVecs = averageOfAssociated.get(i);
                    int[][] currAverage = getAverage(currentVecs);
                    newAverages.add(i, currAverage);
                }

                averages = newAverages;
                assign(allVectors, associations);

            } else break;
        }

        writeToFile();

//        if (averages.size() == 16) {
//            for (int i = 0; i < averages.size(); i++) {
//                int[][] block = averages.get(i);
//                for (int j = 0; j < block.length; j++) {
//                    for (int k = 0; k < block.length; k++) {
//                        System.out.print(block[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//
//            for (int i = 0; i < associations.length; i++) {
//                System.out.print(associations[i] + " ");
//            }
//        }


    }


    public static void main(String[] args) {
        Compression comp = new Compression(2, 64, "G:\\College\\Year 3\\Fall\\Information Theory and Data Compression\\simple.jpg");
        //comp.getAverage(comp.partition());
        comp.compress();
    }


}
