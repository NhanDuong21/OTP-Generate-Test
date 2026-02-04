/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.security.SecureRandom;

public class OtpUtil {

    private static final SecureRandom RNG = new SecureRandom();

    public static String generate6Digits() {
        int n = RNG.nextInt(1_000_000); // 0..999999
        return String.format("%06d", n);
    }
}
