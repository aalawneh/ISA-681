package edu.gmu.isa681.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;

import java.util.Arrays;

public class QuickPasswordEncodingGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String password = "password";
		String decode;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		System.out.println("The encoded password = " + encodedPassword);
		
		CharSequence rawPassword = encodedPassword;
		byte[] decodePassword = Hex.decode(rawPassword);
		decode = Arrays.toString(decodePassword);
		System.out.println("The encoded password = " + decode);
	}
}
