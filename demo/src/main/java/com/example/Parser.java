package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    public String name;
    public static final int TRACKCUTOFF = 50;
    public static Set<String> products = new HashSet<>();
    public static Set<String> animals = new HashSet<>();
    public static Set<String> parts = new HashSet<>();
    public static Set<Integer> originYears = new HashSet<>();
    public static Set<Integer> developmentYears = new HashSet<>();
    public static HashMap<String, Animal> allAnimals = new HashMap<String, Animal>();

    public Parser(String filename) {
        this.name = filename;
    }

    /**
     * 
     * @param filename
     * @throws IOException
     */
    public static void parse(String filename) throws IOException {

        ArrayList<String> content = (ArrayList<String>) Files
                .readAllLines(Paths.get(System.getProperty("user.dir") + File.separator + filename));

        ArrayList<String[]> rows = new ArrayList<String[]>();

        int filesize = content.size();
        System.out.println("file size:" + content.size());
        // Remove empty lines (have size of 1)
        for (int i = 0; i < content.size(); i++) {
            String[] row = content.get(i).replaceAll("\"", " ").split(",");
            if (row.length == 1) {
                writeToFile("No tracks at frame:" + i + "\n", "Track_log.txt");
                // System.out.println("No tracks at frame:" + i);
            } else {
                rows.add(row);
            }

        }

        double precentageTracks = rows.size() / filesize;
        writeToFile("Precentage of frames with tracks: " + precentageTracks, "Track_log.txt");

        if (precentageTracks < TRACKCUTOFF) {
            writeToFile("The tracking rate is below " + TRACKCUTOFF + "% condsider: " + "\n" +
                    "- relabelling or " + "\n" +
                    "- increasing labelled parts" + "\n" +
                    "- increasing training iterations", "Track_log.txt");
        }

        // Check the file is not empty
        if (rows.size() <= 3) {
            throw new IllegalArgumentException("The file has no tracks");
        }

        System.out.println(Arrays.toString(rows.get(4)));

        HashSet<String> nameRow = getAnimals(rows.get(1));
        HashSet<String> partRow = getParts(rows.get(2));

        // Create all the animal instances
        for (int i = 0; i < animals.size(); i++) {
            System.out.println(i);
            Animal animal = makeAnimal(rows, i);
        }

        System.out.println(animals);
        System.out.println(parts);
        System.out.println("file size:" + rows.size());
        // System.out.println(animalZero.records);
        // NB: The empty bits are equal to ""

        // for (int j = 1; j < content.size(); j++) {

        // // Get every row of the file
        // String[] row = content.get(j).replaceAll("\"", " ").split(",");

        // if (row == null) {
        // System.out.println("Error message: " + "Empty row, Value Ignored in line" +
        // j);
        // continue;
        // }

        // // Check validity of origin or development year
        // if (row[1] == null || row[2] == null) {
        // System.out.println("Error message: " + "Invalid value of Origin year [" +
        // row[1] + "] or development year ["
        // + row[2] + "], Value Ignored in line " + j);
        // continue;
        // }

        // int originYear;
        // int developmentYear;

        // try {
        // originYear = Integer.valueOf(row[1].trim());
        // developmentYear = Integer.valueOf(row[2].trim());
        // } catch (NumberFormatException e) {
        // // handle exception
        // System.out.println("Error message: " + "Invalid types of Origin year [" +
        // row[1] + "] or development year ["
        // + row[2] + "], Values Ignored in line " + j);
        // continue;
        // }

        // // Check validity of origin or development year
        // if (originYear < 0 || developmentYear < 0) {
        // System.out.println("Error message: " + "Invalid value of Origin year [" +
        // row[1] + "] or development year ["
        // + row[2] + "], Value Ignored");
        // continue;
        // }

        // // Check if valid combination of origin and development years
        // if (originYear > developmentYear) {
        // System.out.println("Error message: " + "Origin Year [" + row[1] +
        // "] greater than Development Year ["
        // + row[2] + "] Value Ignored in line " + j);
        // continue;
        // }

        // ArrayList<Integer> keys = new ArrayList<Integer>();
        // keys.add(originYear);
        // keys.add(developmentYear);

        // double payment;
        // try {
        // payment = Double.valueOf(row[3]);
        // } catch (NumberFormatException e) {
        // // handle exception
        // System.out.println("Error message: " + "Invalid types of Origin year [" +
        // row[1] + "] or development year ["
        // + row[2] + "], Values Ignored in line" + j);
        // continue;

        // }

        // // Update the origin and development year lists
        // originYears.add(originYear);
        // developmentYears.add(developmentYear);

        // makeProduct(row[0], keys, payment);

        // }

    }

    /**
     * 
     * @param productName
     * @param yearPairs
     * @param payment
     */

    // private static void makeProduct(String productName, ArrayList<Integer>
    // yearPairs, double payment) {

    // // Create product objects or update their records
    // if (!products.contains(productName)) {
    // Product product = new Product(productName);
    // allProducts.put(productName, product);
    // product.getRecords().put(yearPairs, payment);
    // products.add(productName);
    // } else {
    // allProducts.get(productName).getRecords().put(yearPairs, payment);
    // products.add(productName);
    // }

    // }

    /**
     * 
     * @return
     */
    public Set<String> getProducts() {
        return products;
    }

    /**
     * 
     * @return
     */
    public HashMap<String, Animal> getAllAnimals() {
        return allAnimals;
    }

    /**
     * 
     * @return
     */
    public Set<Integer> getOriginYears() {
        return originYears;
    }

    /**
     * 
     * @return
     */
    public Set<Integer> getDevelopmentYears() {
        return developmentYears;
    }

    /**
     * 
     * @param line
     * @return
     */
    private static HashSet<String> getAnimals(String[] line) {

        List<String> list = Arrays.asList(line);
        HashSet<String> nameSet = new HashSet<>();
        nameSet.addAll(list);

        nameSet.remove("individuals");
        animals = nameSet;
        return nameSet;
    }

    /**
     * 
     * @param line
     * @return
     */
    private static HashSet<String> getParts(String[] line) {

        List<String> list = Arrays.asList(line);
        HashSet<String> partSet = new HashSet<>();
        partSet.addAll(list);

        partSet.remove("bodyparts");

        parts = partSet;
        return partSet;
    }

    /**
     * 
     * @param file
     * @param animal
     * @return
     */
    private static Animal makeAnimal(ArrayList<String[]> file, int animal) {

        int individualDataSize = parts.size() * 3;
        int index = animal * individualDataSize + 1;

        String name = file.get(1)[index];
        Animal newAnimal = new Animal(name);
        System.out.println(name);
        allAnimals.put(name, newAnimal);
        HashMap<String, ArrayList<ArrayList<Double>>> partTracks = new HashMap<String, ArrayList<ArrayList<Double>>>();

        // For every body part labelled for animal at index get the bodyparts and their
        // coordinates in all the frames
        for (int i = 0; i < parts.size(); i++) {

            int relativeIndex = index + (3 * i);
            String part = file.get(2)[relativeIndex];
            System.out.println("part: " + part);
            ArrayList<ArrayList<Double>> partCoordinates = new ArrayList<ArrayList<Double>>();

            // Traverse through the file and get the coordinates for the bodypart at index i
            for (int j = 4; j < file.size(); j++) {
                String[] valuesRow = file.get(j);
                ArrayList<Double> coordinates = new ArrayList<Double>();
                // System.out.println("Line:" + j);

                if ((relativeIndex < valuesRow.length) && !valuesRow[relativeIndex].equals("")
                        && !valuesRow[relativeIndex + 1].equals("")) {
                    coordinates.add(Double.parseDouble(valuesRow[relativeIndex]));
                    coordinates.add(Double.parseDouble(valuesRow[relativeIndex + 1]));
                    partCoordinates.add(coordinates);
                }

            }

            newAnimal.records.put(part, partCoordinates);
        }
        return newAnimal;
    }

    /**
     * 
     * @param output
     */
    public static void writeToFile(String output, String filename) {

        File file = new File(filename);
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}