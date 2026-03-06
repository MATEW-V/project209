import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

// I want to create a simple binary conversion quiz.
// It should also test on floating points and its conversions.
// It can be expanded upon in the future but for now its multiple choice. (made the 2 classes future proof with extra setters / getters)

// There will be a parent class for binary numbers, including signed and unsigned for 32 bit representation.
// Basic getter methods like getDegree, toDecimal or check if signed. toString should format the binary in groups of 4 bits.

// A child class for IEEE 754 that calculates Exponent with excess 127 and its mantissa.
// It inherits the parent methods to work. toString should give scientific notation, and has a getter for the IEEE754 format in straight binary string.
// Can also overload and provide sign, mantissa, exponent. (may not have a use but can be expanded upon)

public class ProjectOneTester {
    private static Random rand = new Random();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Binary Quiz ===");
            System.out.println("1. Random Question");
            System.out.println("2. Exit");
            System.out.print("Choose: ");

            int choice = getIntInput(2);
            if (choice == 2)
                return;
            randomQuestion();
        }
    }

    // input for user checks
    private static int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                System.out.print("Invalid! Enter a number: ");
                scanner.nextLine(); // clear buffer
            }
        }
    }

    private static int getIntInput(int max) {
        while (true) {
            int input = getIntInput();
            if (input >= 1 && input <= max) {
                return input;
            }
            System.out.print("Enter 1-" + max + ": ");
        }
    }

    // random binary method
    private static String randBin(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
            sb.append(rand.nextInt(2));
        return sb.toString();
    }

    // padder for the random when ieee
    private static String pad32(String b) {
        while (b.length() < 32)
            b = "0" + b;
        return b;
    }

    // quiz interface w/ arraylists
    private static void quiz(String q, ArrayList<?> opts, Object correct, String msg) {
        Collections.shuffle(opts);
        System.out.println("\n" + q);
        for (int i = 0; i < opts.size(); i++)
            System.out.println((i + 1) + ". " + opts.get(i));
        System.out.print("Choice: ");

        int choice = getIntInput(opts.size());

        if (opts.get(choice - 1).equals(correct))
            System.out.println("✓ Correct!");
        else
            System.out.println("✗ Wrong. " + msg);
    }

    // question bank (each one uses the classes to make answers and false ones)
    public static void randomQuestion() {
        String bin = randBin(8 + rand.nextInt(25));
        String padded = pad32(bin);
        IEEE754Conversion ieee = new IEEE754Conversion(padded);
        boolean signed = rand.nextBoolean();

        switch (rand.nextInt(6)) {
            case 0: // binary to decimal
                long dec = new BinaryConversion(bin, signed).toDecimal();
                ArrayList<Long> opts1 = new ArrayList<>();
                opts1.add(dec);
                opts1.add(dec + rand.nextInt(10) + 5);
                opts1.add(dec - rand.nextInt(10) - 5);
                opts1.add(new BinaryConversion(randBin(rand.nextInt(16) + 1), signed).toDecimal());
                quiz("Binary (" + (signed ? "signed" : "unsigned") + "): " + bin + "\nDecimal?", opts1, dec,
                        "Answer: " + dec);
                break;

            case 1: // decimal to Binary
                int d = signed ? rand.nextInt(201) - 100 : rand.nextInt(256);
                String correctBin = Integer.toBinaryString(Math.abs(d));
                if (signed && d < 0)
                    correctBin = Integer.toBinaryString(~Math.abs(d) + 1).substring(24);
                ArrayList<String> opts2 = new ArrayList<>();
                opts2.add(correctBin);
                opts2.add(Integer.toBinaryString(d + rand.nextInt(5) + 1));
                opts2.add(Integer.toBinaryString(d - rand.nextInt(5) - 1));
                opts2.add(new BinaryConversion(randBin(rand.nextInt(16) + 1), signed).getBinary());
                quiz("Decimal (" + (signed ? "signed" : "unsigned") + "): " + d + "\nBinary?", opts2, correctBin,
                        "Answer: " + correctBin);
                break;

            case 2: // IEEE754 sign bit
                String sign = ieee.getSignBit().equals("0") ? "Positive" : "Negative";
                ArrayList<String> opts3 = new ArrayList<>();
                opts3.add(sign);
                opts3.add(sign.equals("Positive") ? "Negative" : "Positive");
                quiz("IEEE754: " + ieee.getIEEE754Format() + "\nSign?", opts3, sign, "It's " + sign);
                break;

            case 3: // IEEE754 exponent
                int exp = Integer.parseInt(ieee.getExponent(), 2) - 127;
                ArrayList<Integer> opts4 = new ArrayList<>();
                opts4.add(exp);
                opts4.add(exp + rand.nextInt(5) + 1);
                opts4.add(exp - rand.nextInt(5) - 1);
                opts4.add(rand.nextInt(20) - 10);
                quiz("Exponent bits: " + ieee.getExponent() + "\nActual exponent?", opts4, exp, "Answer: " + exp);
                break;

            case 4: // binary to Scientific
                String sci = ieee.toString();
                ArrayList<String> opts5 = new ArrayList<>();
                opts5.add(sci);

                char[] expFlip = ieee.getExponent().toCharArray();
                expFlip[rand.nextInt(8)] ^= 1;
                opts5.add(new IEEE754Conversion(ieee.getSignBit(), new String(expFlip), ieee.getMantissa()).toString());

                char[] manFlip = ieee.getMantissa().toCharArray();
                manFlip[rand.nextInt(23)] ^= 1;
                opts5.add(new IEEE754Conversion(ieee.getSignBit(), ieee.getExponent(), new String(manFlip)).toString());

                opts5.add(new IEEE754Conversion(rand.nextBoolean() ? "0" : "1", randBin(8), randBin(23)).toString());
                quiz("Binary: " + bin + "\nScientific?", opts5, sci, "Correct: " + sci);
                break;

            case 5: // degree
                int deg = new BinaryConversion(bin, false).getDegree();
                ArrayList<Integer> opts7 = new ArrayList<>();
                opts7.add(deg);
                opts7.add(deg + rand.nextInt(3) + 1);
                opts7.add(deg - rand.nextInt(3) - 1);
                opts7.add(rand.nextInt(10));
                quiz("Binary: " + bin + "\nDegree (2^?)", opts7, deg, "Degree: 2^" + deg);
                break;
        }
    }
}