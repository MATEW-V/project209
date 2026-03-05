public class BinaryConversion {
    private String binary;
    private boolean isSigned; 
    
    // default contructor: unsigned
    public BinaryConversion(String binary) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("not binary");
        }
        this.binary = binary;
        this.isSigned = false;
    }

    // overloaded constructor with sign and magnitude
    public BinaryConversion(String binary, boolean signed) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("invalid binary string");
        }
        this.binary = binary;
        this.isSigned = signed;
    }
    
    public void setSigned(boolean signed) { this.isSigned = signed; } // sign bool setter

    public boolean isSigned() { return isSigned; }  // sign checker
    
    // signed decimal conv
    public long toDecimal() {
        if (!isSigned) {
            // unsigned
            return Long.parseLong(binary, 2);
        } else {
            // twos comp
            if (binary.charAt(0) == '0') {
                return Long.parseLong(binary, 2); 
            } else {
                long value = Long.parseLong(binary, 2);
                return value - (1L << binary.length()); 
            }
        }
    }
    
    // get signed decimal value
    public long toSignedDecimal() {
        if (binary.charAt(0) == '0') {
            return Long.parseLong(binary, 2);
        } else {
            long value = Long.parseLong(binary, 2);
            return value - (1L << binary.length());
        }
    }
    //hex conversion
    public String toHex() {
        return Long.toHexString(toDecimal()).toUpperCase();
    }
    
    public int getDegree() {
        return binary.length();
    }
    
    // set binary (preserves current sign setting)
    public void setBinary(String binary) {
        if (!isBinary(binary)) {
            throw new IllegalArgumentException("Invalid binary string");
        }
        this.binary = binary;
    }
    
    public static boolean isBinary(String input) {
        if (input == null || input.isEmpty()) return false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '0' && c != '1') return false;
        }
        return true;
    }
}