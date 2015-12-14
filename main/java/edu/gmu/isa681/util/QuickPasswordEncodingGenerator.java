package edu.gmu.isa681.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;

public class QuickPasswordEncodingGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String password = "password";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		System.out.println("The encoded password = " + encodedPassword);
		
		CharSequence rawPassword = encodedPassword;
		byte[] decodePassword = Hex.decode(rawPassword);
		System.out.println("The encoded password = " + decodePassword.toString());
	}
}