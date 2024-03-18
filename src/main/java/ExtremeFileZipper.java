import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

public class ExtremeFileZipper {
	public static void main(String[] args) {
		String[] filesToZip = { "large_file.csv"};
		String zipFileName = "large_file_zip.zip";
//		String[] filesToZip = { "file1.txt", "file2.txt", "file3.txt" };
//		String zipFileName = "files.zip";
		try {
			zipFiles(filesToZip, zipFileName);
			System.out.println("Files zipped successfully with extreme compression!");
			System.out.println(System.currentTimeMillis());
		} catch (IOException e) {
			System.out.println("Error zipping files: " + e.getMessage());
		}
	}

	public static void zipFiles(String[] filesToZip, String zipFileName) throws IOException {
		byte[] buffer = new byte[1024];
		try (FileOutputStream fos = new FileOutputStream(zipFileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				XZCompressorOutputStream xzOut = new XZCompressorOutputStream(bos);
				ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(xzOut)) {
			for (String file : filesToZip) {
				File inputFile = new File(file);
				FileInputStream fis = new FileInputStream(inputFile);
				ZipArchiveEntry entry = new ZipArchiveEntry(inputFile.getName());
				zipOut.putArchiveEntry(entry);

				int length;
				while ((length = fis.read(buffer)) > 0) {
					zipOut.write(buffer, 0, length);
				}

				fis.close();
				zipOut.closeArchiveEntry();
			}

			zipOut.finish();
		}
	}
}