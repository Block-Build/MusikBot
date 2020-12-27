package de.blockbuild.musikbot.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public abstract class ConfigurationManager {
	private final File file;

	public ConfigurationManager(File file) {
		this.file = file;
	}

	public synchronized boolean saveConfig(Map<String, Object> data, String header) {
		try (BufferedWriter bw = Files.newBufferedWriter(file.toPath())) {

			DumperOptions o = new DumperOptions();
			o.setPrettyFlow(true);
			o.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

			Yaml yaml = new Yaml(o);
			StringWriter writer = new StringWriter();
			writer.append(header);
			yaml.dump(data, writer);

			bw.write(writer.toString());

			return true;
		} catch (Exception e) {
			System.out.println("Couldn't save " + file.getName());
			e.printStackTrace();
			return false;
		}
	}

	public synchronized Map<String, Object> loadConfig() {
		try (InputStream is = new FileInputStream(file)) {
			Yaml yaml = new Yaml();
			return yaml.load(is);

		} catch (FileNotFoundException fnfe) {
			// System.out.println("File not found, creating new one.");
			return new LinkedHashMap<String, Object>();

		} catch (Exception e) {
			System.out.println("Couldn't load " + file.getName());
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean deleteConfig() {
		return file.delete();
	}

	public abstract boolean writeConfig();

	public abstract boolean readConfig();

	public String getRawConfiguration() {
		return loadConfig().toString();
	}
}