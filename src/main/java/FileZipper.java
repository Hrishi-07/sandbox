import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipper {
	public static void main(String[] args) {
		String[] filesToZip = { "C:\\Users\\hrchavan\\Desktop\\large_file.csv"};
		String zipFileName = "C:\\Users\\hrchavan\\Desktop\\large_file_zip.zip";
		try {
			zipFiles(filesToZip, zipFileName);
			System.out.println("Files zipped successfully!");
		} catch (IOException e) {
			System.out.println("Error zipping files: " + e.getMessage());
		}
	}

	public static void zipFiles(String[] filesToZip, String zipFileName) throws IOException {
		byte[] buffer = new byte[1024];
		try (FileOutputStream fos = new FileOutputStream(zipFileName); ZipOutputStream zos = new ZipOutputStream(fos)) {
			for (String file : filesToZip) {
				File inputFile = new File(file);
				FileInputStream fis = new FileInputStream(inputFile);
				zos.putNextEntry(new ZipEntry(inputFile.getName()));

				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}

				fis.close();
				zos.closeEntry();
			}
		}
	}
}