package wow.artificerpi.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Downloader {
	private static final String SELENIUM_RELEASE_URL = "http://selenium-release.storage.googleapis.com/";
	private static final String CHROMEDRIVER_RELEASE_URL = "https://chromedriver.storage.googleapis.com/";

	private static final String SELENIUM_SERVER_FILENAME = "selenium-server-standalone.jar";
	private static final String CHROMEDRIVER_FILENAME = "chromedriver.zip";
	private static final String WEBDRIVER_DIR = "webdriver";

	private static final Logger LOG = Logger.getLogger(Downloader.class.getSimpleName());

	public void downloadSelenium() throws DocumentException, IOException {
		final String SELENIUM_RELEASE_FILE = "selenium-release.xml";
		String releaseFile = WEBDRIVER_DIR + File.separator + SELENIUM_RELEASE_FILE;
		File dstFile = new File(releaseFile);
		URL url = new URL(SELENIUM_RELEASE_URL);
		FileUtils.copyURLToFile(url, dstFile);
		Document doc = DocumentHelper.parseText(FileUtils.readFileToString(dstFile, Charset.defaultCharset()));

		Element root = doc.getRootElement();

		String latestVersion = null;
		String pattern = "^(\\d.\\d)/selenium-server-standalone-(\\d.\\d.\\d).jar$";
		for (Iterator<Element> it = root.elementIterator("Contents"); it.hasNext();) {
			Element foo = it.next();

			String foundName = foo.element("Key").getStringValue();
			if (foundName.matches(pattern)) {
				latestVersion = foundName;
			}
		}

		if (latestVersion == null) {
			System.out.println("Cannot find lastest version");
			return;
		}
		URL lastetFileURL = new URL(SELENIUM_RELEASE_URL + latestVersion);

		File downloadFile = new File(WEBDRIVER_DIR + File.separator + SELENIUM_SERVER_FILENAME);
		LOG.info("Downloading " + lastetFileURL.toString() + "...");
		FileUtils.copyURLToFile(lastetFileURL, downloadFile);
		LOG.info("Finish downloading file " + downloadFile.getAbsolutePath());
	}

	public void downloadChromeDriver() throws IOException {
		String fileName = "chromedriver_latest_version";
		String filePath = WEBDRIVER_DIR + File.separator + fileName;
		URL latestReleaseURL = new URL(CHROMEDRIVER_RELEASE_URL + "LATEST_RELEASE");
		File latestVersionFile = new File(filePath);

		LOG.info("Downloading latest release version file: " + latestReleaseURL.toString());
		FileUtils.copyURLToFile(latestReleaseURL, latestVersionFile);
		LOG.info("finished");

		String latestVersion = FileUtils.readFileToString(latestVersionFile, Charset.defaultCharset());
		latestVersion = latestVersion.trim();
		LOG.info("Found latest version: " + latestVersion);
		// TODO os check here and download
		// chromedriver_linux64.zip

		URL lastetFileURL = new URL(CHROMEDRIVER_RELEASE_URL + latestVersion + "/chromedriver_linux64.zip");

		File downloadFile = new File(WEBDRIVER_DIR + File.separator + CHROMEDRIVER_FILENAME);
		LOG.info("Downloading " + lastetFileURL.toString() + "...");
		FileUtils.copyURLToFile(lastetFileURL, downloadFile);
		LOG.info("Finish downloading file " + downloadFile.getAbsolutePath());

		LOG.info("Unzip " + downloadFile.getAbsolutePath() + " to" + WEBDRIVER_DIR);
		this.unzip(downloadFile, WEBDRIVER_DIR);
	}

	public void unzip(File zipFile, String outputDir) {
		InputStream inputStream = null;
		try {
			inputStream = FileUtils.openInputStream(zipFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		ZipInputStream zis = new ZipInputStream(inputStream);
		try {
			for (ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
				File file = new File(outputDir + File.separator + zipEntry.getName());
				OutputStream out = FileUtils.openOutputStream(file);
				IOUtils.copy(zis, out);

				LOG.info("Inflatting " + file.getName() + " to " + outputDir);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		IOUtils.closeQuietly(zis);
	}
}
