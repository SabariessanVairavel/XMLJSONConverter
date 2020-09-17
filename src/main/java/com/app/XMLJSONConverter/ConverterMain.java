package com.app.XMLJSONConverter;

import java.io.IOException;

public class ConverterMain {

	private static XMLJSONConverterI converter;
	private static String inputPath;
	private static String outputPath;

	public static void main(String[] args) {
		if (args.length != 0) {
			inputPath = args[0];
			outputPath = args[1];
			try {
				converter = new ConverterFactory();
				converter.createXMLJSONConverter(inputPath, outputPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Please provide JSON File path and XML outputpath");
		}
	}

}
