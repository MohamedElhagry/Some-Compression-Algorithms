public class LZ77Tag {
    int pos, len;
    char nextSymbol;


    private String numToBinary(int num) {
        // numbers are written from left to right
        String s = "";
        while (num > 0) {
            s = s + Integer.toString(num % 2);
            num /= 2;
        }
        return s;
    }

    LZ77Tag() {
        pos = 0;
        len = 0;
    }

    LZ77Tag(int pos, int len, char nextSymbol) {
        this.pos = pos;
        this.len = len;
        this.nextSymbol = nextSymbol;
    }

    String ConvertToBinary(int posSize, int lenSize) {


        //padding with zeros to ensure fixed size
        String posStr = numToBinary(pos);
        while (posStr.length() < posSize) posStr = posStr + '0';

        String lenStr = numToBinary(len);
        while (lenStr.length() < lenSize) lenStr = lenStr + '0';

        String charStr = numToBinary(nextSymbol);

        return posStr + lenStr + charStr;
    }

    public static void main(String[] args) {
        LZ77Tag t = new LZ77Tag(3, 6, 'a');
        System.out.println(t.ConvertToBinary(4, 4));
    }


}

