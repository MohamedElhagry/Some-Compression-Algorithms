public class Node_20190725 implements Comparable<Node_20190725> {
    String letters;
    int p;

    Node_20190725(String letters, int p) {
        this.letters = letters;
        this.p = p;
    }


    @Override
    public int compareTo(Node_20190725 n) {
        if (this.p > n.p) return 1;
        if (this.p == n.p) return 0;
        return -1;
    }
}
