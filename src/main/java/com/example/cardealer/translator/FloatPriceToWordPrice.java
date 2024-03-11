package com.example.cardealer.translator;

import java.util.HashMap;
import java.util.Map;

public class FloatPriceToWordPrice {

        private static final Map<Integer, String> ones = new HashMap<>();
        private static final Map<Integer, String> teens = new HashMap<>();
        private static final Map<Integer, String> tens = new HashMap<>();

        static {
            ones.put(0, "");
            ones.put(1, "jeden");
            ones.put(2, "dwa");
            ones.put(3, "trzy");
            ones.put(4, "cztery");
            ones.put(5, "pięć");
            ones.put(6, "sześć");
            ones.put(7, "siedem");
            ones.put(8, "osiem");
            ones.put(9, "dziewięć");

            teens.put(10, "dziesięć");
            teens.put(11, "jedenaście");
            teens.put(12, "dwanaście");
            teens.put(13, "trzynaście");
            teens.put(14, "czternaście");
            teens.put(15, "piętnaście");
            teens.put(16, "szesnaście");
            teens.put(17, "siedemnaście");
            teens.put(18, "osiemnaście");
            teens.put(19, "dziewiętnaście");

            tens.put(2, "dwadzieścia");
            tens.put(3, "trzydzieści");
            tens.put(4, "czterdzieści");
            tens.put(5, "pięćdziesiąt");
            tens.put(6, "sześćdziesiąt");
            tens.put(7, "siedemdziesiąt");
            tens.put(8, "osiemdziesiąt");
            tens.put(9, "dziewięćdziesiąt");
        }

        public static String convert(float number) {
            if (number < 0) {
                return "minus " + convert(-number);
            }

            if (number < 10) {
                return ones.get((int) number);
            }

            if (number < 20) {
                return teens.get((int) number);
            }

            if (number < 100) {
                return tens.get((int) number / 10) + ((number % 10 != 0) ? " " + convert(number % 10) : "");
            }

            if (number < 200) {
                return "sto" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
            }

            if (number < 300) {
                return "dwieście" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
            }

            if (number < 500) {
                return ones.get((int) number / 100) + "sta" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
            }

            if (number < 1000) {
                return ones.get((int) number / 100) + "set" + ((number % 100 != 0) ? " " + convert(number % 100) : "");
            }

            if (number < 1000000) {
                return convert(number / 1000) + " tysięcy" + ((number % 1000 != 0) ? " " + convert(number % 1000) : "");
            }

            if (number < 1000000000) {
                return convert(number / 1000000) + " milionów" + ((number % 1000000 != 0) ? " " + convert(number % 1000000) : "");
            }

            return "number too large to convert";
        }

        public static void main(String[] args) {
            long number = 123456789;
            String words = convert(number);
            System.out.println(number + " = " + words);
        }


}
