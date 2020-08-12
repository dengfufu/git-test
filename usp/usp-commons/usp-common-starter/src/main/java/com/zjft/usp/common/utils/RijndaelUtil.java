package com.zjft.usp.common.utils;



/**
 * Rijndael（AES）加密类
 * @author Administrator
 *
 */
public class RijndaelUtil {

    /**
     * The S box for Encryption and Encryption key generation
     */
    private final static byte[] S = {
            (byte) 0x63, (byte) 0x7c, (byte) 0x77, (byte) 0x7b, (byte) 0xf2,
            (byte) 0x6b, (byte) 0x6f, (byte) 0xc5, (byte) 0x30, (byte) 0x1,
            (byte) 0x67, (byte) 0x2b, (byte) 0xfe, (byte) 0xd7, (byte) 0xab,
            (byte) 0x76, (byte) 0xca, (byte) 0x82, (byte) 0xc9, (byte) 0x7d,
            (byte) 0xfa, (byte) 0x59, (byte) 0x47, (byte) 0xf0, (byte) 0xad,
            (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c, (byte) 0xa4,
            (byte) 0x72, (byte) 0xc0, (byte) 0xb7, (byte) 0xfd, (byte) 0x93,
            (byte) 0x26, (byte) 0x36, (byte) 0x3f, (byte) 0xf7, (byte) 0xcc,
            (byte) 0x34, (byte) 0xa5, (byte) 0xe5, (byte) 0xf1, (byte) 0x71,
            (byte) 0xd8, (byte) 0x31, (byte) 0x15, (byte) 0x4, (byte) 0xc7,
            (byte) 0x23, (byte) 0xc3, (byte) 0x18, (byte) 0x96, (byte) 0x5,
            (byte) 0x9a, (byte) 0x7, (byte) 0x12, (byte) 0x80, (byte) 0xe2,
            (byte) 0xeb, (byte) 0x27, (byte) 0xb2, (byte) 0x75, (byte) 0x9,
            (byte) 0x83, (byte) 0x2c, (byte) 0x1a, (byte) 0x1b, (byte) 0x6e,
            (byte) 0x5a, (byte) 0xa0, (byte) 0x52, (byte) 0x3b, (byte) 0xd6,
            (byte) 0xb3, (byte) 0x29, (byte) 0xe3, (byte) 0x2f, (byte) 0x84,
            (byte) 0x53, (byte) 0xd1, (byte) 0x0, (byte) 0xed, (byte) 0x20,
            (byte) 0xfc, (byte) 0xb1, (byte) 0x5b, (byte) 0x6a, (byte) 0xcb,
            (byte) 0xbe, (byte) 0x39, (byte) 0x4a, (byte) 0x4c, (byte) 0x58,
            (byte) 0xcf, (byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb,
            (byte) 0x43, (byte) 0x4d, (byte) 0x33, (byte) 0x85, (byte) 0x45,
            (byte) 0xf9, (byte) 0x2, (byte) 0x7f, (byte) 0x50, (byte) 0x3c,
            (byte) 0x9f, (byte) 0xa8, (byte) 0x51, (byte) 0xa3, (byte) 0x40,
            (byte) 0x8f, (byte) 0x92, (byte) 0x9d, (byte) 0x38, (byte) 0xf5,
            (byte) 0xbc, (byte) 0xb6, (byte) 0xda, (byte) 0x21, (byte) 0x10,
            (byte) 0xff, (byte) 0xf3, (byte) 0xd2, (byte) 0xcd, (byte) 0xc,
            (byte) 0x13, (byte) 0xec, (byte) 0x5f, (byte) 0x97, (byte) 0x44,
            (byte) 0x17, (byte) 0xc4, (byte) 0xa7, (byte) 0x7e, (byte) 0x3d,
            (byte) 0x64, (byte) 0x5d, (byte) 0x19, (byte) 0x73, (byte) 0x60,
            (byte) 0x81, (byte) 0x4f, (byte) 0xdc, (byte) 0x22, (byte) 0x2a,
            (byte) 0x90, (byte) 0x88, (byte) 0x46, (byte) 0xee, (byte) 0xb8,
            (byte) 0x14, (byte) 0xde, (byte) 0x5e, (byte) 0xb, (byte) 0xdb,
            (byte) 0xe0, (byte) 0x32, (byte) 0x3a, (byte) 0xa, (byte) 0x49,
            (byte) 0x6, (byte) 0x24, (byte) 0x5c, (byte) 0xc2, (byte) 0xd3,
            (byte) 0xac, (byte) 0x62, (byte) 0x91, (byte) 0x95, (byte) 0xe4,
            (byte) 0x79, (byte) 0xe7, (byte) 0xc8, (byte) 0x37, (byte) 0x6d,
            (byte) 0x8d, (byte) 0xd5, (byte) 0x4e, (byte) 0xa9, (byte) 0x6c,
            (byte) 0x56, (byte) 0xf4, (byte) 0xea, (byte) 0x65, (byte) 0x7a,
            (byte) 0xae, (byte) 0x8, (byte) 0xba, (byte) 0x78, (byte) 0x25,
            (byte) 0x2e, (byte) 0x1c, (byte) 0xa6, (byte) 0xb4, (byte) 0xc6,
            (byte) 0xe8, (byte) 0xdd, (byte) 0x74, (byte) 0x1f, (byte) 0x4b,
            (byte) 0xbd, (byte) 0x8b, (byte) 0x8a, (byte) 0x70, (byte) 0x3e,
            (byte) 0xb5, (byte) 0x66, (byte) 0x48, (byte) 0x3, (byte) 0xf6,
            (byte) 0xe, (byte) 0x61, (byte) 0x35, (byte) 0x57, (byte) 0xb9,
            (byte) 0x86, (byte) 0xc1, (byte) 0x1d, (byte) 0x9e, (byte) 0xe1,
            (byte) 0xf8, (byte) 0x98, (byte) 0x11, (byte) 0x69, (byte) 0xd9,
            (byte) 0x8e, (byte) 0x94, (byte) 0x9b, (byte) 0x1e, (byte) 0x87,
            (byte) 0xe9, (byte) 0xce, (byte) 0x55, (byte) 0x28, (byte) 0xdf,
            (byte) 0x8c, (byte) 0xa1, (byte) 0x89, (byte) 0xd, (byte) 0xbf,
            (byte) 0xe6, (byte) 0x42, (byte) 0x68, (byte) 0x41, (byte) 0x99,
            (byte) 0x2d, (byte) 0xf, (byte) 0xb0, (byte) 0x54, (byte) 0xbb,
            (byte) 0x16};

    /**
     * The inverse S box for Decryption and Decryption key generation
     */
    private final static byte[] Si = {
            (byte) 0x52, (byte) 0x9, (byte) 0x6a, (byte) 0xd5, (byte) 0x30,
            (byte) 0x36, (byte) 0xa5, (byte) 0x38, (byte) 0xbf, (byte) 0x40,
            (byte) 0xa3, (byte) 0x9e, (byte) 0x81, (byte) 0xf3, (byte) 0xd7,
            (byte) 0xfb, (byte) 0x7c, (byte) 0xe3, (byte) 0x39, (byte) 0x82,
            (byte) 0x9b, (byte) 0x2f, (byte) 0xff, (byte) 0x87, (byte) 0x34,
            (byte) 0x8e, (byte) 0x43, (byte) 0x44, (byte) 0xc4, (byte) 0xde,
            (byte) 0xe9, (byte) 0xcb, (byte) 0x54, (byte) 0x7b, (byte) 0x94,
            (byte) 0x32, (byte) 0xa6, (byte) 0xc2, (byte) 0x23, (byte) 0x3d,
            (byte) 0xee, (byte) 0x4c, (byte) 0x95, (byte) 0xb, (byte) 0x42,
            (byte) 0xfa, (byte) 0xc3, (byte) 0x4e, (byte) 0x8, (byte) 0x2e,
            (byte) 0xa1, (byte) 0x66, (byte) 0x28, (byte) 0xd9, (byte) 0x24,
            (byte) 0xb2, (byte) 0x76, (byte) 0x5b, (byte) 0xa2, (byte) 0x49,
            (byte) 0x6d, (byte) 0x8b, (byte) 0xd1, (byte) 0x25, (byte) 0x72,
            (byte) 0xf8, (byte) 0xf6, (byte) 0x64, (byte) 0x86, (byte) 0x68,
            (byte) 0x98, (byte) 0x16, (byte) 0xd4, (byte) 0xa4, (byte) 0x5c,
            (byte) 0xcc, (byte) 0x5d, (byte) 0x65, (byte) 0xb6, (byte) 0x92,
            (byte) 0x6c, (byte) 0x70, (byte) 0x48, (byte) 0x50, (byte) 0xfd,
            (byte) 0xed, (byte) 0xb9, (byte) 0xda, (byte) 0x5e, (byte) 0x15,
            (byte) 0x46, (byte) 0x57, (byte) 0xa7, (byte) 0x8d, (byte) 0x9d,
            (byte) 0x84, (byte) 0x90, (byte) 0xd8, (byte) 0xab, (byte) 0x0,
            (byte) 0x8c, (byte) 0xbc, (byte) 0xd3, (byte) 0xa, (byte) 0xf7,
            (byte) 0xe4, (byte) 0x58, (byte) 0x5, (byte) 0xb8, (byte) 0xb3,
            (byte) 0x45, (byte) 0x6, (byte) 0xd0, (byte) 0x2c, (byte) 0x1e,
            (byte) 0x8f, (byte) 0xca, (byte) 0x3f, (byte) 0xf, (byte) 0x2,
            (byte) 0xc1, (byte) 0xaf, (byte) 0xbd, (byte) 0x3, (byte) 0x1,
            (byte) 0x13, (byte) 0x8a, (byte) 0x6b, (byte) 0x3a, (byte) 0x91,
            (byte) 0x11, (byte) 0x41, (byte) 0x4f, (byte) 0x67, (byte) 0xdc,
            (byte) 0xea, (byte) 0x97, (byte) 0xf2, (byte) 0xcf, (byte) 0xce,
            (byte) 0xf0, (byte) 0xb4, (byte) 0xe6, (byte) 0x73, (byte) 0x96,
            (byte) 0xac, (byte) 0x74, (byte) 0x22, (byte) 0xe7, (byte) 0xad,
            (byte) 0x35, (byte) 0x85, (byte) 0xe2, (byte) 0xf9, (byte) 0x37,
            (byte) 0xe8, (byte) 0x1c, (byte) 0x75, (byte) 0xdf, (byte) 0x6e,
            (byte) 0x47, (byte) 0xf1, (byte) 0x1a, (byte) 0x71, (byte) 0x1d,
            (byte) 0x29, (byte) 0xc5, (byte) 0x89, (byte) 0x6f, (byte) 0xb7,
            (byte) 0x62, (byte) 0xe, (byte) 0xaa, (byte) 0x18, (byte) 0xbe,
            (byte) 0x1b, (byte) 0xfc, (byte) 0x56, (byte) 0x3e, (byte) 0x4b,
            (byte) 0xc6, (byte) 0xd2, (byte) 0x79, (byte) 0x20, (byte) 0x9a,
            (byte) 0xdb, (byte) 0xc0, (byte) 0xfe, (byte) 0x78, (byte) 0xcd,
            (byte) 0x5a, (byte) 0xf4, (byte) 0x1f, (byte) 0xdd, (byte) 0xa8,
            (byte) 0x33, (byte) 0x88, (byte) 0x7, (byte) 0xc7, (byte) 0x31,
            (byte) 0xb1, (byte) 0x12, (byte) 0x10, (byte) 0x59, (byte) 0x27,
            (byte) 0x80, (byte) 0xec, (byte) 0x5f, (byte) 0x60, (byte) 0x51,
            (byte) 0x7f, (byte) 0xa9, (byte) 0x19, (byte) 0xb5, (byte) 0x4a,
            (byte) 0xd, (byte) 0x2d, (byte) 0xe5, (byte) 0x7a, (byte) 0x9f,
            (byte) 0x93, (byte) 0xc9, (byte) 0x9c, (byte) 0xef, (byte) 0xa0,
            (byte) 0xe0, (byte) 0x3b, (byte) 0x4d, (byte) 0xae, (byte) 0x2a,
            (byte) 0xf5, (byte) 0xb0, (byte) 0xc8, (byte) 0xeb, (byte) 0xbb,
            (byte) 0x3c, (byte) 0x83, (byte) 0x53, (byte) 0x99, (byte) 0x61,
            (byte) 0x17, (byte) 0x2b, (byte) 0x4, (byte) 0x7e, (byte) 0xba,
            (byte) 0x77, (byte) 0xd6, (byte) 0x26, (byte) 0xe1, (byte) 0x69,
            (byte) 0x14, (byte) 0x63, (byte) 0x55, (byte) 0x21, (byte) 0xc,
            (byte) 0x7d};

    /**
     * The logarithm table for multiplication
     */
    private final static int[] log = {
            0x0, 0x0, 0x19, 0x1, 0x32, 0x2,
            0x1a, 0xc6, 0x4b, 0xc7, 0x1b, 0x68,
            0x33, 0xee, 0xdf, 0x3, 0x64, 0x4,
            0xe0, 0xe, 0x34, 0x8d, 0x81, 0xef,
            0x4c, 0x71, 0x8, 0xc8, 0xf8, 0x69,
            0x1c, 0xc1, 0x7d, 0xc2, 0x1d, 0xb5,
            0xf9, 0xb9, 0x27, 0x6a, 0x4d, 0xe4,
            0xa6, 0x72, 0x9a, 0xc9, 0x9, 0x78,
            0x65, 0x2f, 0x8a, 0x5, 0x21, 0xf,
            0xe1, 0x24, 0x12, 0xf0, 0x82, 0x45,
            0x35, 0x93, 0xda, 0x8e, 0x96, 0x8f,
            0xdb, 0xbd, 0x36, 0xd0, 0xce, 0x94,
            0x13, 0x5c, 0xd2, 0xf1, 0x40, 0x46,
            0x83, 0x38, 0x66, 0xdd, 0xfd, 0x30,
            0xbf, 0x6, 0x8b, 0x62, 0xb3, 0x25,
            0xe2, 0x98, 0x22, 0x88, 0x91, 0x10,
            0x7e, 0x6e, 0x48, 0xc3, 0xa3, 0xb6,
            0x1e, 0x42, 0x3a, 0x6b, 0x28, 0x54,
            0xfa, 0x85, 0x3d, 0xba, 0x2b, 0x79,
            0xa, 0x15, 0x9b, 0x9f, 0x5e, 0xca,
            0x4e, 0xd4, 0xac, 0xe5, 0xf3, 0x73,
            0xa7, 0x57, 0xaf, 0x58, 0xa8, 0x50,
            0xf4, 0xea, 0xd6, 0x74, 0x4f, 0xae,
            0xe9, 0xd5, 0xe7, 0xe6, 0xad, 0xe8,
            0x2c, 0xd7, 0x75, 0x7a, 0xeb, 0x16,
            0xb, 0xf5, 0x59, 0xcb, 0x5f, 0xb0,
            0x9c, 0xa9, 0x51, 0xa0, 0x7f, 0xc,
            0xf6, 0x6f, 0x17, 0xc4, 0x49, 0xec,
            0xd8, 0x43, 0x1f, 0x2d, 0xa4, 0x76,
            0x7b, 0xb7, 0xcc, 0xbb, 0x3e, 0x5a,
            0xfb, 0x60, 0xb1, 0x86, 0x3b, 0x52,
            0xa1, 0x6c, 0xaa, 0x55, 0x29, 0x9d,
            0x97, 0xb2, 0x87, 0x90, 0x61, 0xbe,
            0xdc, 0xfc, 0xbc, 0x95, 0xcf, 0xcd,
            0x37, 0x3f, 0x5b, 0xd1, 0x53, 0x39,
            0x84, 0x3c, 0x41, 0xa2, 0x6d, 0x47,
            0x14, 0x2a, 0x9e, 0x5d, 0x56, 0xf2,
            0xd3, 0xab, 0x44, 0x11, 0x92, 0xd9,
            0x23, 0x20, 0x2e, 0x89, 0xb4, 0x7c,
            0xb8, 0x26, 0x77, 0x99, 0xe3, 0xa5,
            0x67, 0x4a, 0xed, 0xde, 0xc5, 0x31,
            0xfe, 0x18, 0xd, 0x63, 0x8c, 0x80,
            0xc0, 0xf7, 0x70, 0x7};

    /**
     * The anti logarithm table for multiplication
     */
    private final static int[] alog = {
            0x1, 0x3, 0x5, 0xf, 0x11, 0x33,
            0x55, 0xff, 0x1a, 0x2e, 0x72, 0x96,
            0xa1, 0xf8, 0x13, 0x35, 0x5f, 0xe1,
            0x38, 0x48, 0xd8, 0x73, 0x95, 0xa4,
            0xf7, 0x2, 0x6, 0xa, 0x1e, 0x22,
            0x66, 0xaa, 0xe5, 0x34, 0x5c, 0xe4,
            0x37, 0x59, 0xeb, 0x26, 0x6a, 0xbe,
            0xd9, 0x70, 0x90, 0xab, 0xe6, 0x31,
            0x53, 0xf5, 0x4, 0xc, 0x14, 0x3c,
            0x44, 0xcc, 0x4f, 0xd1, 0x68, 0xb8,
            0xd3, 0x6e, 0xb2, 0xcd, 0x4c, 0xd4,
            0x67, 0xa9, 0xe0, 0x3b, 0x4d, 0xd7,
            0x62, 0xa6, 0xf1, 0x8, 0x18, 0x28,
            0x78, 0x88, 0x83, 0x9e, 0xb9, 0xd0,
            0x6b, 0xbd, 0xdc, 0x7f, 0x81, 0x98,
            0xb3, 0xce, 0x49, 0xdb, 0x76, 0x9a,
            0xb5, 0xc4, 0x57, 0xf9, 0x10, 0x30,
            0x50, 0xf0, 0xb, 0x1d, 0x27, 0x69,
            0xbb, 0xd6, 0x61, 0xa3, 0xfe, 0x19,
            0x2b, 0x7d, 0x87, 0x92, 0xad, 0xec,
            0x2f, 0x71, 0x93, 0xae, 0xe9, 0x20,
            0x60, 0xa0, 0xfb, 0x16, 0x3a, 0x4e,
            0xd2, 0x6d, 0xb7, 0xc2, 0x5d, 0xe7,
            0x32, 0x56, 0xfa, 0x15, 0x3f, 0x41,
            0xc3, 0x5e, 0xe2, 0x3d, 0x47, 0xc9,
            0x40, 0xc0, 0x5b, 0xed, 0x2c, 0x74,
            0x9c, 0xbf, 0xda, 0x75, 0x9f, 0xba,
            0xd5, 0x64, 0xac, 0xef, 0x2a, 0x7e,
            0x82, 0x9d, 0xbc, 0xdf, 0x7a, 0x8e,
            0x89, 0x80, 0x9b, 0xb6, 0xc1, 0x58,
            0xe8, 0x23, 0x65, 0xaf, 0xea, 0x25,
            0x6f, 0xb1, 0xc8, 0x43, 0xc5, 0x54,
            0xfc, 0x1f, 0x21, 0x63, 0xa5, 0xf4,
            0x7, 0x9, 0x1b, 0x2d, 0x77, 0x99,
            0xb0, 0xcb, 0x46, 0xca, 0x45, 0xcf,
            0x4a, 0xde, 0x79, 0x8b, 0x86, 0x91,
            0xa8, 0xe3, 0x3e, 0x42, 0xc6, 0x51,
            0xf3, 0xe, 0x12, 0x36, 0x5a, 0xee,
            0x29, 0x7b, 0x8d, 0x8c, 0x8f, 0x8a,
            0x85, 0x94, 0xa7, 0xf2, 0xd, 0x17,
            0x39, 0x4b, 0xdd, 0x7c, 0x84, 0x97,
            0xa2, 0xfd, 0x1c, 0x24, 0x6c, 0xb4,
            0xc7, 0x52, 0xf6, 0x1};

    /**
     * The constants table for key generation
     */
    private final static byte[] rcon = {
            (byte) 0x1, (byte) 0x2, (byte) 0x4, (byte) 0x8, (byte)
            0x10, (byte) 0x20,
            (byte) 0x40, (byte) 0x80, (byte) 0x1b, (byte) 0x36, (byte)
            0x6c, (byte) 0xd8,
            (byte) 0xab, (byte) 0x4d, (byte) 0x9a, (byte) 0x2f, (byte)
            0x5e, (byte) 0xbc,
            (byte) 0x63, (byte) 0xc6, (byte) 0x97, (byte) 0x35, (byte)
            0x6a, (byte) 0xd4,
            (byte) 0xb3, (byte) 0x7d, (byte) 0xfa, (byte) 0xef, (byte)
            0xc5, (byte) 0x91};

    /**
     * Returns the product of two binary numbers a and b mod 255
     * @param a operand for multiply.
     * @param b operand for multiply.
     * @return the result of (a * b) % 255.
     */
    int mul(int a, int b) {
        return (a != 0 && b != 0)
                ? alog[(log[a & 0xFF] + log[b & 0xFF]) % 255]
                : 0;
    }

    /**
     * implements subBytes transform of Rijndael
     * Substituites for bytes from input array from S-box
     *
     * @param txt the two dimensional byte array in which the bytes need
     *            to be substituited
     *            after the application of this function, txt has new set of values
     */
    private void subBytes(byte[][] txt) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                txt[i][j] = S[txt[i][j] & 0xFF];
            }
        }
    }// end subBytes

    /**
     * implements inverse of subBytes transform of Rijndael,
     * <p/>
     * Substituites for bytes from input array from inverse S-box
     * <p/>
     *
     * @param txt the two dimensional byte array in which the bytes need
     *            to be substituited
     *            after the application of this function, txt has new set of values
     */
    private void invSubBytes(byte[][] txt) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                txt[i][j] = Si[txt[i][j] & 0xFF];
            }
        }
    }


    /**
     * implements shiftRows transform of Rijndael,
     * <p/>
     * Shifts the second, third and fourth rows by one two and three places
     * respectively
     * <p/>
     *
     * @param txt the two dimensional byte array in which the bytes need
     *            to be substituited
     *            after the application of this function, txt has second, third and
     *            fourth rows rotated
     */
    private void shiftRows(byte[][] txt) {
        byte[][] tmp = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = txt[i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                txt[i][j] = tmp[i][(i + j) % 4];
            }
        }
    } // end shiftRows

    /**
     * implements inverse shiftRows transform of Rijndael,
     * <p/>
     * undoes the shifts in the rows caused by the shiftRows function
     * <p/>
     *
     * @param txt the two dimensional byte array in which the bytes need
     *            to be substituited
     *            after the application of this function, txt has second, third and
     *            fourth rows rotated
     *            to obtain original txt array, i.e., was before the application of
     *            shiftRows tranform
     */
    private void invShiftRows(byte[][] txt) {
        byte[][] tmp = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = txt[i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                txt[i][(i + j) % 4] = tmp[i][j];
            }
        }
    }

    /**
     * implements mixColumns transform of Rijndael
     * <p/>
     * xor's columns of txt[][] array with appropriate scheduled keys
     * <p/>
     *
     * @param txt the two dimensional byte array in which the rows need
     *            xor'ed
     *            after the application of this function, rows of txt are throroughly
     *            mixed with
     *            appropriate round keys
     */
    private void mixColumns(byte[][] txt) {
        byte[][] tmp = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = txt[i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            txt[0][i] = (byte) (mul(2, tmp[0][i]) ^ mul(3, tmp[1][i]) ^ tmp[2][i]
                    ^ tmp[3][i]);
            txt[1][i] = (byte) (mul(2, tmp[1][i]) ^ mul(3, tmp[2][i]) ^ tmp[0][i]
                    ^ tmp[3][i]);
            txt[2][i] = (byte) (mul(2, tmp[2][i]) ^ mul(3, tmp[3][i]) ^ tmp[1][i]
                    ^ tmp[0][i]);
            txt[3][i] = (byte) (mul(2, tmp[3][i]) ^ mul(3, tmp[0][i]) ^ tmp[2][i]
                    ^ tmp[1][i]);
        }
    }

    /**
     * implements inverse mixColumns transform of Rijndael
     * <p/>
     * xor's columns of txt[][] array with appropriate scheduled keys
     * <p/>
     *
     * @param txt the two dimensional byte array in which the rows need
     *            xor'ed
     *            after the application of this function, rows of txt are throroughly
     *            mixed with
     *            appropriate round keys
     */
    private void invMixColumns(byte[][] txt) {
        byte[][] tmp = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = txt[i][j];
            }
        }
        for (int i = 0; i < 4; i++) {
            txt[0][i] = (byte) (mul(0x0E, tmp[0][i]) ^ mul(0x0B, tmp[1][i]) ^
                    mul(0x0D, tmp[2][i]) ^ mul(0x09, tmp[3][i]));
            txt[1][i] = (byte) (mul(0x09, tmp[0][i]) ^ mul(0x0E, tmp[1][i]) ^
                    mul(0x0B, tmp[2][i]) ^ mul(0x0D, tmp[3][i]));
            txt[2][i] = (byte) (mul(0x0D, tmp[0][i]) ^ mul(0x09, tmp[1][i]) ^
                    mul(0x0E, tmp[2][i]) ^ mul(0x0B, tmp[3][i]));
            txt[3][i] = (byte) (mul(0x0B, tmp[0][i]) ^ mul(0x0D, tmp[1][i]) ^
                    mul(0x09, tmp[2][i]) ^ mul(0x0E, tmp[3][i]));
        }
    }


    /**
     * implements Encryption\Decryption key generation algorithm of
     * Rijndael
     * <p/>
     * generates round keys of each round from an user's input
     * <p/>
     *
     * @param key the two integer array supplied by the user
     *            after the application of this function, a two dimensional array of
     *            size 44/4 is generated.
     * @throws java.lang.Exception is thrown when the user supplied
     *                             array has a length other than 16
     */
    protected byte[][] keySchedule(byte[] key) {
        if (key.length != 16) {
            key="wsprintfwsprintf".getBytes();
        }
        byte[][] w = new byte[44][4];
        int i = 0;
        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                w[i][j] = key[4 * i + j];
            }
        }
        byte[] temp = null;
        for (i = 4; i < 44; i++) {


            temp = new byte[w[i - 1].length];
            for (int i2 = 0; i2 < temp.length; i2++) {
                temp[i2] = w[i - 1][i2];
            }

            if (i % 4 == 0) {
                rotWord(temp);
                subWord(temp);
                temp[0] ^= rcon[(i / 4) - 1];
            }
            for (int k = 0; k < w[i].length; k++) {
                w[i][k] = (byte) (w[i - 4][k] ^ temp[k]);
            }
        }

        return w;
    }

    private void addRoundKey(byte[][] txt, byte[][] w, int round) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                txt[i][j] ^= w[(round * 4 + j)][i];
            }
        }
    }

    /**
     * A convenience method for key generation
     */
    private void rotWord(byte[] w) {
        //if (w.length != 4) ;//throw new Exception("rotation requires a byte array of length 4");
        byte temp = w[0];
        w[0] = w[1];
        w[1] = w[2];
        w[2] = w[3];
        w[3] = temp;
    }

    /**
     * A convenience method for key generation
     */
    private void subWord(byte[] w) {
        for (int i = 0; i < w.length; i++) {
            w[i] = S[w[i] & 0xFF];
        }
    }

    /**
     * This method encrypts 128 bits (16 bytes) of data and outputs
     * corresponding cipher text
     *
     * @param txt is a 16 byte array given bye user as input, contains
     *            plain text to be encrypted
     * @param cpr is a 16 byte array that stores the cipher text output
     * @param key is a two dimensional byte array, an output of
     *            keySchedule routine
     */
    public void blockEncrypt(byte[] txt, byte[] cpr, byte[][] key) {
        byte[][] state = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = txt[i + 4 * j];
            }
        }
        addRoundKey(state, key, 0);
        for (int i = 1; i <= 9; i++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, key, i);
        }
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, key, 10);
        for (int j = 0; j < 4; j++) {
            for (int l = 0; l < 4; l++) {
                cpr[j + 4 * l] = state[j][l];
            }
        }
    }


    /**
     * This method decrypts 128 bits (16 bytes) of data and outputs
     * corresponding cipher text
     *
     * @param cpr is a 16 byte array given bye user as input, contains
     *            cipher text to be decrypted
     * @param txt is a 16 byte array that stores the plain text output
     * @param key is a two dimensional byte array, an output of
     *            keySchedule routine
     */
    public void blockDecrypt(byte[] cpr, byte[] txt, byte[][] key) {
        byte[][] state = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = cpr[i + 4 * j];
            }
        }
        addRoundKey(state, key, 10);
        //System.out.println("Decryption");
        //System.out.println(printHex(cpr));
        for (int i = 9; i > 0; i--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, key, i);
            invMixColumns(state);
        }
        invSubBytes(state);
        invShiftRows(state);
        addRoundKey(state, key, 0);
        for (int j = 0; j < 4; j++) {
            for (int l = 0; l < 4; l++) {
                txt[j + 4 * l] = state[j][l];
            }
        }
    }

    ///////////////////////////////////////////////////////

    // 使用方法
    private final static RijndaelUtil r = new RijndaelUtil();

    // 加密
    public static byte[] encrypt(byte[] txt, byte[] key, byte padding) {
        byte[][] k = new byte[0][];
        int nSize = txt.length / 16;
        int nLeft = txt.length % 16;
        byte[] cpr = new byte[txt.length + (nLeft == 0 ? 0 : (16 - nLeft))];
        int L = 0;
        byte[] src = new byte[16];
        byte[] dest = new byte[16];
        k = r.keySchedule(key);
        for (int i = 0; i < nSize; i++) {
            System.arraycopy(txt, L, src, 0, 16);
            r.blockEncrypt(src, dest, k);
            System.arraycopy(dest, 0, cpr, L, 16);
            L += 16;
        }
        if (nLeft > 0) {
            System.arraycopy(txt, L, src, 0, nLeft);
            for (int i = nLeft; i < 16; i++) {
                src[i] = padding;
            }
            r.blockEncrypt(src, dest, k);
            System.arraycopy(dest, 0, cpr, L, 16);
            L += 16;
        }
        return cpr;
    }

    // 解密
    public static byte[] decrypt(byte[] cpr, byte[] key) {
        int nSize = cpr.length / 16;
        if (nSize == 0) {
            return null;
        }
        byte[] txt = new byte[nSize * 16];
        int L = 0;
        byte[] src = new byte[16];
        byte[] dest = new byte[16];
        byte[][] k = r.keySchedule(key);
        for (int i = 0; i < nSize; i++) {
            System.arraycopy(cpr, L, src, 0, 16);
            r.blockDecrypt(src, dest, k);
            System.arraycopy(dest, 0, txt, L, 16);
            L += 16;
        }
        return txt;
    }

}