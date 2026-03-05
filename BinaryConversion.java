public class BinaryConversion {
    private String binary;
    private boolean isSigned; 
    
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
    }
    
    // signed decimal conv
    public long toDecimal() {
        if (!isSigned) {
            // unsigned
            long value = Long.parseLong(binary, 2);
            if (binary.length() == 32 && binary.charAt(0) == '1') {
                return value + (1L << 32);
            }
        return value;
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
    
    public String toHex() { return Long.toHexString(toDecimal()).toUpperCase(); }   //hex conversion
    public String getBinary() { return this.binary;}                                //basic get binary no formatting
    public int getDegree() { return binary.length(); }                              //for floating points
    public void setSigned(boolean signed) { this.isSigned = signed; }               // sign bool setter
    public boolean isSigned() { return isSigned; }                                  // sign checker

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

    @Override
    public String toString() { 
        String extended32 = this.binary;
        
        if (extended32.length() < 32) {
            char padChar = isSigned ? extended32.charAt(0) : '0';
            while (extended32.length() < 32) {
                extended32 = padChar + extended32;
            }
        }
        // spaces every 4 bits
        String res = "";
        for (int i = 0; i < 32; i++) {
            if (i > 0 && i % 4 == 0) {
                res += " ";
            }
            res += extended32.charAt(i);
        }
        return res;
    }

}