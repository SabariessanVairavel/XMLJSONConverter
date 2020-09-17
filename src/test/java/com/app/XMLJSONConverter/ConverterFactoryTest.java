package com.app.XMLJSONConverter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ConverterFactoryTest{

	XMLJSONConverterI test = new ConverterFactory();
	
	private void testXMLJSONConverter() {
		String inputPath = "F:\\Sabari\\AR\\examples\\example.json";
		String outputPath = "F:\\Sabari\\AR\\examples\\example.xml";
		File file1 = new File(outputPath);
		File file2 = new File("F:\\Sabari\\AR\\examples\\example_test.xml");
		try {
			test.createXMLJSONConverter(inputPath, outputPath);
			FileUtils.contentEquals(file1, file2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		testXMLJSONConverter();
	}
}
