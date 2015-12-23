package net.qiujuer.sample.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {
		File in = new File("R.txt");
		File out = new File("public.xml");

		if (!in.exists()) {
			throw new NullPointerException("R.txt is not null.");
		}

		try {
			out.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(in.getAbsolutePath());
		System.out.println(out.getAbsolutePath());

		InputStreamReader read = new InputStreamReader(new FileInputStream(in));
		BufferedReader bufferedReader = new BufferedReader(read);

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(out));
		BufferedWriter bufferedWriter = new BufferedWriter(writer);

		Map<String, PublicLine> xml = new HashMap<>();
		buildXml(bufferedReader, xml);

		List<PublicLine> lines = new ArrayList<>();
		lines.addAll(xml.values());

		Collections.sort(lines);

		saveFile(lines, bufferedWriter);

		close(bufferedReader);
		close(bufferedWriter);

		System.out.println("End.");
	}

	public static void buildXml(BufferedReader reader, Map<String, PublicLine> xml) {
		while (true) {
			String line;
			try {
				line = reader.readLine();
				if (line == null || line.trim().length() == 0)
					return;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			if (line.contains("styleable")) {
				// skip styleable array
				continue;
			} else {
				// convert other xml
				String[] split = line.split(" ");
				if (split.length == 0)
					continue;

				String type = split[1];
				String name = split[2];
				String id = split[3];
				if (type.contains("style"))
					name = name.replace("_", ".");
				saveToMap(xml, new PublicLine(type, name, id));
			}

			/**
			String[] split = line.split(" ");
			if (split.length == 0)
				continue;

			if (line.contains("int[]")) {
				// convert attr xml
				String name = split[2].trim();
				line = line.substring(line.indexOf("{") + 1, line.lastIndexOf("}"));
				System.out.println(line);

				String[] ids = line.split(",");
				if (ids.length > 0) {
					readStyleableXml(reader, xml, ids, name);
				}
			} else {
				// convert other xml
				String type = split[1];
				String name = split[2];
				String id = split[3];
				if (type.contains("style"))
					name = name.replace("_", ".");
				saveToMap(xml, new PublicLine(type, name, id));
			}
			**/
		}
	}

	@SuppressWarnings("unused")
	public static void readStyleableXml(BufferedReader reader, Map<String, PublicLine> xml, String[] ids, String name) {
		for (String id : ids) {
			String line;
			try {
				line = reader.readLine();
				if (line == null)
					continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			String[] split = line.split(" ");

			String lName = split[2].substring(split[2].indexOf(name) + name.length() + 1);
			String lId = ids[Integer.parseInt(split[3].trim())];
			saveToMap(xml, new PublicLine("attr", lName, lId));
		}
	}

	public static void saveToMap(Map<String, PublicLine> xml, PublicLine line) {
		try {
			xml.putIfAbsent(line.getKey(), line);
			System.out.println(">>>: " + line.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveFile(List<PublicLine> lines, BufferedWriter writer) throws IOException {
		// write head
		writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.append("\n");
		writer.append("<resources>");
		writer.append("\n");

		for (PublicLine line : lines) {
			try {
				writer.append("	");
				writer.append(line.toString());
				writer.append("\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// write footer
		writer.append("</resources>");
		writer.flush();
	}

	public static void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class PublicLine implements Comparable<PublicLine> {
		public String type;
		public String name;
		public String id;

		public PublicLine() {
		}

		public PublicLine(String type, String name, String id) {
			this.type = type.trim();
			this.name = name.trim();
			this.id = id.trim();
		}

		public String getKey() {
			return type + "_" + name;
		}

		@Override
		public String toString() {
			return "<public type=\"" + type + "\" name=\"" + name + "\" id=\"" + id + "\" />";
		}

		@Override
		public int compareTo(PublicLine o) {
			int i = this.type.compareTo(o.type);
			if (i == 0)
				return this.name.compareTo(o.name);
			else
				return i;
		}
	}

}
