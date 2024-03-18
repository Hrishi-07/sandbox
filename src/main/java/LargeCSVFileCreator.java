import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LargeCSVFileCreator {
    public static void main(String[] args) {
        // Set the size of the CSV file in gigabytes
        long fileSizeGB = 5;
        // Define the number of rows you want
        int numRows = 1000000; // You may adjust this based on your needs

        // Specify the CSV file path
        String csvFilePath = "large_file.csv";

        // Create and write to the CSV file
        try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
            // Write header
            csvWriter.append("Column1,Column2,Column3\n");

            // Write data rows
            for (int i = 0; i < numRows; i++) {
                String row = generateRandomData() + "," + generateRandomData() + "," + generateRandomData() + "\n";
                csvWriter.append(row);
            }

            System.out.println("CSV file created at: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate random data for the CSV file
    private static String generateRandomData() {
        int length = 10;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomData = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomData.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomData.toString();
    }
}
