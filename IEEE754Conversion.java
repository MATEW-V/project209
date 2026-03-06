public class IEEE754Conversion extends BinaryConversion {
    
    private String exponent;
    private String mantissa;
    
    public IEEE754Conversion(String binary) {
        super(binary, true);
        if (binary.length() != 32) {
            throw new IllegalArgumentException("IEEE-754 requires exactly 32 bits");
        }
        this.exponent = calculateExponent();
        this.mantissa = calculateMantissa();
    }
    
    public IEEE754Conversion(String sign, String exponent, String mantissa) {
        super(sign + exponent + mantissa, true);
        if (sign.length() != 1 || exponent.length() != 8 || mantissa.length() != 23) {
            throw new IllegalArgumentException("Invalid component lengths");
        }
        if (!isBinary(sign) || !isBinary(exponent) || !isBinary(mantissa)) {
            throw new IllegalArgumentException("All components must be binary strings");
        }
        this.exponent = exponent;
        this.mantissa = mantissa;
    }
    
    private String calculateExponent() {
        long deg = getDegree();
        long biased = deg + 127;
        String expRes = Long.toBinaryString(biased);
        while (expRes.length() < 8) {
            expRes = "0" + expRes;
        }
        return expRes;
    }

    private String calculateMantissa() {
        String binary = getBinary(); 
        String mantissa = binary.substring(1);
        if (mantissa.length() < 23) {
            while (mantissa.length() < 23) {
                mantissa += "0";
            }
        } else if (mantissa.length() > 23) { 
            mantissa = mantissa.substring(0, 23);
        }
        return mantissa;
    }
    
    public String getSignBit() { 
        return String.valueOf(getBinary().charAt(0)); 
    }
    
    public String getExponent() { 
        return exponent; 
    }
    
    public String getMantissa() { 
        return mantissa; 
    }
    
    public String getIEEE754Format() { 
        return getSignBit() + exponent + mantissa; 
    }
    
    @Override
    public String toString() {
        String sign = getSignBit().equals("1") ? "-" : "";
        int biasedExp = Integer.parseInt(exponent, 2);
        int actualExp = biasedExp - 127;
        String mantissaTrimmed = mantissa.replaceAll("0+$", "");
        String mantissaDisplay = "1." + (mantissaTrimmed.isEmpty() ? "0" : mantissaTrimmed);
        return String.format("%s%s × 2^%d", sign, mantissaDisplay, actualExp);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof IEEE754Conversion)) return false;
        IEEE754Conversion other = (IEEE754Conversion) obj;
        return exponent.equals(other.exponent) && mantissa.equals(other.mantissa);
    }
}