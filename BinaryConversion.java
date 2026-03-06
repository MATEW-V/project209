import java.util.ArrayList;

interface Convertible { // interface here, if i make an octal class it would be useful ig
    long toDecimal();

    String toHex();

    String getBinary();
}

public class BinaryConversion implements Convertible {
    private String binary;
    private boolean isSigned;
    private static ArrayList<String> history = new ArrayList<>(); // Collection

    // default constructor: unsigned
    public BinaryConversion(String binary) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("not binary");
        }
        if (binary.length() > 32) {
            throw new IllegalArgumentException("binary string cannot exceed 32 bits");
        }
        this.binary = binary;
        this.isSigned = false;
        history.add(binary + " (unsigned)"); // Use collection
    }

    // overloaded constructor but signed
    public BinaryConversion(String binary, boolean signed) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("invalid binary string");
        }
        if (binary.length() > 32) {
            throw new IllegalArgumentException("binary string cannot exceed 32 bits");
        }
        this.binary = binary;
        this.isSigned = signed;
        history.add(binary + (signed ? " (signed)" : " (unsigned)")); // Use collection
    }

    // from interface
    public long toDecimal() {
        if (!isSigned) {
            long value = Long.parseLong(binary, 2);
            if (binary.length() == 32 && binary.charAt(0) == '1') {
                return value + (1L << 32);
            }
            return value;
        } else {
            if (binary.charAt(0) == '0') {
                return Long.parseLong(binary, 2);
            } else {
                long value = Long.parseLong(binary, 2);
                return value - (1L << binary.length());
            }
        }
    }

    // from interface
    public String toHex() {
        return Long.toHexString(toDecimal()).toUpperCase();
    }

    // from interface
    public String getBinary() {
        return this.binary;
    }

    public int getDegree() {
        return binary.length() - 1;
    }

    public void setSigned(boolean signed) {
        this.isSigned = signed;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setBinary(String binary) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("Invalid binary string");
        }
        this.binary = binary;
    }

    public static boolean isBinary(String input) {
        if (input == null || input.isEmpty())
            return false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '0' && c != '1')
                return false;
        }
        return true;
    }

    public static void printHistory() {
        System.out.println("Binary Conversion History: " + history);
    }

    @Override
    public String toString() {
        String extended32 = this.binary;
        if (extended32.length() < 32) {
            char padChar = isSigned ? extended32.charAt(0) : '0';
            while (extended32.length() < 32) {
                extended32 = padChar + extended32;
            }
        }
        String res = "";
        for (int i = 0; i < 32; i++) {
            if (i > 0 && i % 4 == 0) {
                res += " ";
            }
            res += extended32.charAt(i);
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BinaryConversion other = (BinaryConversion) obj;
        return binary.equals(other.binary) && isSigned == other.isSigned;
    }
}