package ch03.springbook.calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filePath) throws IOException {

		return lineReadTemplate(
			filePath,
			new LineCallBack() {
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {
					return value + Integer.valueOf(line);
				}
			},
			0
	   );
	}

	public Integer calcMultiply(String filePath) throws IOException {

		return lineReadTemplate(
			filePath,
			new LineCallBack() {
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {
					return value * Integer.valueOf(line);
				}
			},
			1
	   );
	}

	public Integer lineReadTemplate(String filePath, LineCallBack lineCallBack, int initVal) throws IOException {

		BufferedReader br = null;

		try {

			br = new BufferedReader(new FileReader(filePath));

			Integer res = initVal;
			String line = null;

			while ((line = br.readLine()) != null) {
				res = lineCallBack.doSomethingWithLine(line, res);
			}

			return res;

		} catch (IOException e) {
			throw e;

		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
}
