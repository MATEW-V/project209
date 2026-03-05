public class IEEE754Conversion extends BinaryConversion {
    
    public IEEE754Conversion(String binary) {
        super(binary, true); // IEEE-754 always uses signed interpretation
        if (binary.length() != 32) {
            throw new IllegalArgumentException("IEEE-754 requires exactly 32 bits");
        }
    }
    
    public String getSignBit() {
        return String.valueOf(getBinary().charAt(0));
    }
    
    
    public double getDecimalValue() {
        return -1.0;
    }
    
    @Override
    public String toString() {
        return "";
    }
    
}