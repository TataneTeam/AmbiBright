package ambibright;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ambibright.ressources.ToolFile;

public class Updater {
	public static void main(String[] args) throws Exception {
		// Downloading the zip file
		String dlPath = ToolFile.downloadFile("https://raw.github.com/TataneTeam/AmbiBright/master/AmbiBright" + ".zip", ToolFile.getNewTempFile());

		// Removing the old lib
		File libFolder = new File("lib");
		if (libFolder.isDirectory()) {
			for (File file : libFolder.listFiles()) {
				file.delete();
			}
		}

		// Decompressing the archive
		ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(dlPath)));
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				new File(ze.getName()).mkdirs();
			}
			// we overwrite all the files but *.bat
			else if (!ze.getName().endsWith(".bat")) {
				File f = new File(ze.getName());
				OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));

				try {
					try {
						final byte[] buf = new byte[8192];
						int bytesRead;
						while (-1 != (bytesRead = zis.read(buf))) {
							fos.write(buf, 0, bytesRead);
						}
					} finally {
						fos.close();
					}
				} catch (final IOException ioe) {
					// en cas d'erreur on efface le fichier
					f.delete();
					throw ioe;
				}
			}
		}

		new File(dlPath).delete();

		Runtime.getRuntime().exec("AmbiBright.bat").waitFor();
	}
}