package com.instahash.api.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Read {

    private List<String> line;

    public Read(String file, String delimiter){
        String csvFile = file;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = delimiter;
//        System.out.println("Reading file...");

        try {

            br = new BufferedReader(new FileReader(csvFile));
            this.line = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] dataArray = line.split(cvsSplitBy);
                this.line.add(line);

            }

//            System.out.println("Reading complete");
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getLine() {
        return line;
    }
}