public class IEEE754Conversion extends BinaryConversion {

    private String exponent;
    private String mantissa;

    // constructor that takes regualr binary
    public IEEE754Conversion(String binary) {
        super(binary, true);
        if (binary.length() != 32) {
            throw new IllegalArgumentException("IEEE-754 requires exactly 32 bits");
        }
        // Calculate from binary
        this.exponent = calculateExponent();
        this.mantissa = calculateMantissa();
    }

    // overloaded constructor - takes sign, exponent (excess-127), and mantissa
    public IEEE754Conversion(String sign, String exponent, String mantissa) {
        super(sign + exponent + mantissa, true);

        // validate lengths
        if (sign.length() != 1) {
            throw new IllegalArgumentException("Sign bit must be exactly 1 bit");
        }
        if (exponent.length() != 8) {
            throw new IllegalArgumentException("Exponent must be exactly 8 bits");
        }
        if (mantissa.length() != 23) {
            throw new IllegalArgumentException("Mantissa must be exactly 23 bits");
        }

        if (!isBinary(sign) || !isBinary(exponent) || !isBinary(mantissa)) {
            throw new IllegalArgumentException("All components must be binary strings");
        }
        this.exponent = exponent;
        this.mantissa = mantissa;
    }

    private String calculateExponent() {
        long deg = getDegree(); // reusing getDegree

        // add bias and convert to binary (only doing single pres for now)
        long biased = deg + 127;
        String expRes = Long.toBinaryString(biased);

        // Pad to 8 bits
        while (expRes.length() < 8) {
            expRes = "0" + expRes;
        }
        return expRes;
    }

    private String calculateMantissa() {
        String binary = getBinary();
        // remove the first bit (implicit leading 1) to get the fractional part
        String mantissa = binary.substring(1);

        if (mantissa.length() < 23) {
            // Pad with trailing zeros
            while (mantissa.length() < 23) {
                mantissa += "0";
            }
        } else if (mantissa.length() > 23) {
            mantissa = mantissa.substring(0, 23); // cut that shi off
        }
        return mantissa;
    }

    // getters
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
    public String toString() { // scientific notation with trailing 0s removed
        String sign = getSignBit().equals("1") ? "-" : "";
        int biasedExp = Integer.parseInt(exponent, 2);
        int actualExp = biasedExp - 127;
        String mantissaTrimmed = mantissa.replaceAll("0+$", "");
        String mantissaDisplay = "1." + mantissaTrimmed;

        return String.format("%s%s × 2^%d", sign, mantissaDisplay, actualExp);
    }
}