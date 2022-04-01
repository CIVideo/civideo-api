package com.jjss.civideo;

import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@ActiveProfiles("test")
class CivideoApplicationTests {

	@Test
	public void givenUsingJava8_whenGeneratingRandomAlphanumericString_thenCorrect() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

		System.out.println(generatedString);
	}

}
