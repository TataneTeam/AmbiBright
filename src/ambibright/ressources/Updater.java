package ambibright.ressources;

import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class Updater {

	public static final String updateJarUrl = "AmbiBright.jar";

	public static final String updateVersionUrl = "lastcompiled.txt";

	public static final String manifestAttribute = "Implementation-Version";

	public String url;

	public Updater(String url) {
		super();
		this.url = url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String getJarLocation() {
		String result = "";
		try {
			result = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toString().replace("file:/", "").replace("/", ToolFile.fileSeparator + "").replace("%20", " ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getLocalVersion() {
		return getLocalVersion(getJarLocation());
	}

	private String getLocalVersion(String jarLocation) {
		String result = "";
		try {
			JarFile jar = new JarFile(jarLocation);
			result = jar.getManifest().getMainAttributes().getValue(manifestAttribute);
		} catch (Exception e) {
		}
		return result;
	}

	public String getServerVersion() {
		String result = "";
		try {
			result = ToolFile.getDownloadedFileContent(url + updateVersionUrl).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean isUpdateAvailable() {
		boolean result = false;
		try {
			String local = getLocalVersion(getJarLocation());
			String server = getServerVersion();
			System.out.println("Local version is " + local);
			System.out.println("Server version is " + server);
			if (local != null && server != null && local.trim().length() > 0 && server.trim().length() > 0 && !local.equals(server)) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean update() {
		boolean result = false;
		String dlPath = ToolFile.downloadFile(url + updateJarUrl, ToolFile.getNewTempFile());
		if (dlPath != null) {
			result = ToolFile.copyfile(dlPath, getJarLocation());
		}
		return result;
	}

	public void manage() {
		if (isUpdateAvailable()) {
			Object[] options = { "Yes, update it!", "No, thanks!" };
			int n = JOptionPane.showOptionDialog(null, "A new update is available.\n" + "Local : " + getLocalVersion() + "\n" + "Server : " + getServerVersion() + "\n" + "Do you want to update?", Factory.appName, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n == 0) {
				if (update()) {
					JOptionPane.showMessageDialog(null, getJarLocation() + " updated.\nYou need to restart the application!", Factory.appName, JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Error occured while updating!", Factory.appName, JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "No update available!", Factory.appName, JOptionPane.INFORMATION_MESSAGE);
		}
	}

}