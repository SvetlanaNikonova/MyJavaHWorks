package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CreateFile {

    public static void main(String[] args) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream("myFile.csv", true);
        PrintWriter pw = new PrintWriter(fos);

        pw.println("EmployeeIdentifier, ContributionDate, ContributionDescription, ContributionAmount, PlanName, PriorTaxYear");
        pw.println("1011915, 12152017, Employer, 3430, HSA(HSA), Current");
        System.out.println("File was created");

        pw.close();

        File sourceFile = new File( "myFile.csv");
        File destinationFile = new File("C:" + File.separator + "Users" + File.separator + "SvetlanaNikonova" +
                File.separator + "Downloads" + File.separator + "myFile.csv");

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File was copied successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(destinationFile));
            while ((line = reader.readLine()) != null) {

                String[] row = line.split(",");
                for (String index : row) {
                    System.out.printf("%-25s", index);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}