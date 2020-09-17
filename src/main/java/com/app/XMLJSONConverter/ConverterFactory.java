package com.app.XMLJSONConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class ConverterFactory implements XMLJSONConverterI {

	public void createXMLJSONConverter(String inputpath, String outputPath) throws IOException {
		File file = new File(inputpath);
		if (file.exists()) {
			String content = new String(Files.readAllBytes(Paths.get(inputpath, new String[0])));
			Gson gson = new Gson();
			StringBuilder xmlContent = new StringBuilder();
			if (content.startsWith("{")) {
				Map<Object, Object> valueMap = gson.fromJson(content, Map.class);
				xmlContent.append("<object>");
				setMapValues(valueMap, xmlContent);
				xmlContent.append("</object>");
			} else if (content.startsWith("[")) {
				List<Object> valueList = gson.fromJson(content, List.class);
				xmlContent.append("<array>");
				setListValues(valueList, xmlContent);
				xmlContent.append("</array>");
			} else if (content.equals("null")) {
				xmlContent.append("<null/>");
			}
			File outputFile = new File(outputPath);
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			FileWriter writer = new FileWriter(outputFile);
			writer.write(xmlContent.toString());
			writer.close();
			System.out.println("Converted output File Path : " + outputPath);
		} else {
			System.out.println("JSON File is not present in the given path");
		}
	}

	private static void setMapValues(Map<Object, Object> valueMap, StringBuilder xmlContent) {
		for (Map.Entry<Object, Object> entry : valueMap.entrySet()) {
			if (Objects.isNull(entry.getValue())) {
				xmlContent.append("\n <null name=\"" + entry.getKey() + "\">" + "\n </null>");
			} else if (entry.getValue() instanceof String) {
				xmlContent.append(
						"\n <string name=\"" + entry.getKey() + "\">" + "\n" + entry.getValue() + "\n </string>");
			} else if (entry.getValue() instanceof Double) {
				xmlContent.append(
						"\n <number name=\"" + entry.getKey() + "\">" + "\n" + entry.getValue() + "\n </number>");
			} else if (entry.getValue() instanceof Boolean) {
				xmlContent.append(
						"\n <boolean name=\"" + entry.getKey() + "\">" + "\n" + entry.getValue() + "\n </boolean>");
			} else if (entry.getValue() instanceof Map) {
				xmlContent.append("\n <object name=\"" + entry.getKey() + "\">");
				setMapValues((Map<Object, Object>) entry.getValue(), xmlContent);
				xmlContent.append("\n </object>");
			} else {
				if (!(entry.getValue() instanceof List)) {
					continue;
				}
				xmlContent.append("\n <array name=\"" + entry.getKey() + "\">");
				setListValues(entry.getValue(), xmlContent);
				xmlContent.append("\n </array>");
			}
		}
	}

	private static void setListValues(Object value, StringBuilder xmlContent) {
		List<Object> valueList = (List<Object>) value;
		if (valueList.size() > 0) {
			for (Object result : valueList) {
				if (Objects.isNull(result)) {
					xmlContent.append("\n <null></null>");
				} else if (result instanceof String) {
					xmlContent.append("\n <string>\n" + result + "\n </string>");
				} else if (result instanceof Double) {
					xmlContent.append("\n <number>\n" + result + "\n </number>");
				} else if (result instanceof Boolean) {
					xmlContent.append("\n <boolean>\n" + result + "\n </boolean>");
				} else if (result instanceof Map) {
					xmlContent.append("\n <object>");
					setMapValues((Map<Object, Object>) result, xmlContent);
					xmlContent.append("\n </object>");
				} else {
					if (!(result instanceof List)) {
						continue;
					}
					xmlContent.append("\n <array>");
					setListValues(result, xmlContent);
					xmlContent.append("\n </array>");
				}
			}
		}
	}
}
