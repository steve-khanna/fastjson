// Copyright 2018 Ulf Adams
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.alibaba.fastjson.util;

import jdk.internal.util.xml.impl.Pair;

import java.math.BigInteger;

/**
 * An implementation of Ryu for double.
 */
public final class RyuDouble extends RyuBridgeAPI{
    private static final int[][] POW5_SPLIT = new int[326][4];
    private static final int[][] POW5_INV_SPLIT = new int[291][4];

    static {
        BigInteger mask = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        BigInteger invMask = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        for (int i = 0; i < 326; i++) {
            BigInteger pow = BigInteger.valueOf(5).pow(i);
            int pow5len = pow.bitLength();
            int expectedPow5Bits = i == 0 ? 1 : (int) ((i * 23219280L + 10000000L - 1) / 10000000L);
            if (expectedPow5Bits != pow5len) {
                throw new IllegalStateException(pow5len + " != " + expectedPow5Bits);
            }
            if (i < POW5_SPLIT.length) {
                for (int j = 0; j < 4; j++) {
                    POW5_SPLIT[i][j] = pow
                            .shiftRight(pow5len - 121 + (3 - j) * 31)
                            .and(mask)
                            .intValue();
                }
            }

            if (i < POW5_INV_SPLIT.length) {
                // We want floor(log_2 5^q) here, which is pow5len - 1.
                int j = pow5len + 121;
                BigInteger inv = BigInteger.ONE
                        .shiftLeft(j)
                        .divide(pow)
                        .add(BigInteger.ONE);
                for (int k = 0; k < 4; k++) {
                    if (k == 0) {
                        POW5_INV_SPLIT[i][k] = inv
                                .shiftRight((3 - k) * 31)
                                .intValue();
                    } else {
                        POW5_INV_SPLIT[i][k] = inv
                                .shiftRight((3 - k) * 31)
                                .and(invMask)
                                .intValue();
                    }
                }
            }
        }
    }

    public static String toString(double value) {
        char[] result = new char[24];
        int len = toString(value, result, 0);
        return new String(result, 0, len);
    }

    public static int toString(double value, char[] result, int off) {
        final long DOUBLE_MANTISSA_MASK = 4503599627370495L; // (1L << 52) - 1;
        final int DOUBLE_EXPONENT_MASK = 2047; // (1 << 11) - 1;
        final int DOUBLE_EXPONENT_BIAS = 1023; // (1 << (11 - 1)) - 1;
        final long LOG10_5_NUMERATOR = 6989700L; // (long) (10000000L * Math.log10(5));
        final long LOG10_2_NUMERATOR = 3010299L; // (long) (10000000L * Math.log10(2));

        // Step 1: Decode the floating point number, and unify normalized and subnormal cases.
        // First, handle all the trivial cases.
        int index = off;
        if (Double.isNaN(value)) {
            result[index++] = 'N';
            result[index++] = 'a';
            result[index++] = 'N';
            return index - off;
        }

        if (value == Double.POSITIVE_INFINITY) {
            result[index++] = 'I';
            result[index++] = 'n';
            result[index++] = 'f';
            result[index++] = 'i';
            result[index++] = 'n';
            result[index++] = 'i';
            result[index++] = 't';
            result[index++] = 'y';
            return index - off;
        }

        if (value == Double.NEGATIVE_INFINITY) {
            result[index++] = '-';
            result[index++] = 'I';
            result[index++] = 'n';
            result[index++] = 'f';
            result[index++] = 'i';
            result[index++] = 'n';
            result[index++] = 'i';
            result[index++] = 't';
            result[index++] = 'y';
            return index - off;
        }

        long bits = Double.doubleToLongBits(value);
        if (bits == 0) {
            result[index++] = '0';
            result[index++] = '.';
            result[index++] = '0';
            return index - off;
        }
        if (bits == 0x8000000000000000L) {
            result[index++] = '-';
            result[index++] = '0';
            result[index++] = '.';
            result[index++] = '0';
            return index - off;
        }

        final int DOUBLE_MANTISSA_BITS = 52;
        // Otherwise extract the mantissa and exponent bits and run the full algorithm.
        int ieeeExponent = (int) ((bits >>> DOUBLE_MANTISSA_BITS) & DOUBLE_EXPONENT_MASK);
        long ieeeMantissa = bits & DOUBLE_MANTISSA_MASK;
        int e2;
        long m2;
        if (ieeeExponent == 0) {
            // Denormal number - no implicit leading 1, and the exponent is 1, not 0.
            e2 = 1 - DOUBLE_EXPONENT_BIAS - DOUBLE_MANTISSA_BITS;
            m2 = ieeeMantissa;
        } else {
            // Add implicit leading 1.
            e2 = ieeeExponent - DOUBLE_EXPONENT_BIAS - DOUBLE_MANTISSA_BITS;
            m2 = ieeeMantissa | (1L << DOUBLE_MANTISSA_BITS);
        }

        boolean sign = bits < 0;

        // Step 2: Determine the interval of legal decimal representations.
        boolean even = (m2 & 1) == 0;
        final long mv = 4 * m2;
        final long mp = 4 * m2 + 2;
        final int mmShift = ((m2 != (1L << DOUBLE_MANTISSA_BITS)) || (ieeeExponent <= 1)) ? 1 : 0;
        final long mm = 4 * m2 - 1 - mmShift;
        e2 -= 2;

        RyuDoubleAlgorithm algo = new RyuDoubleAlgorithm();
        BridgeStruct stepStruct = algo.step3(e2, mv, mp, mm, even,mmShift);

        stepStruct = algo.step4(stepStruct);


        return algo.step5(stepStruct, sign, result, index, off);
    }
}
