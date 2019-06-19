package com.questionbank.util;

import java.util.Random;

public class QUtils {

	public static String getOTP() {
		Random random = new Random();
		String id = String.format("%04d", random.nextInt(10000));
		return id;
	}
}
