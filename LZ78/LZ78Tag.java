public class LZ78Tag {
    int index;
    char nextSymbol;

    private String numToBinary(int num) {
        // numbers are written from left to right (left is least significant)
        String s = "";
        while (num > 0) {
            s = s + Integer.toString(num % 2);
            num /= 2;
        }
        return s;
    }

    LZ78Tag() {
        index = 0;
    }

    LZ78Tag(int index, char nextSymbol) {
        this.index = index;
        this.nextSymbol = nextSymbol;
    }

    String ConvertToBinary(int indexSize) {
        //padding with zeros to ensure fixed size
        String indStr = numToBinary(index);
        while (indStr.length() < indexSize) indStr = indStr + '0';

        String charStr = numToBinary(nextSymbol);

        return indStr + charStr;
    }

    public static void main(String[] args) {
        LZ78Tag t = new LZ78Tag(3, 'a');
        System.out.println(t.ConvertToBinary(4));
    }


}
