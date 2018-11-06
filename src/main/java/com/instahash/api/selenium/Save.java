package com.instahash.api.selenium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Save {

    private File file;
    private FileWriter fw;
    private BufferedWriter writer;

    private void createFile(File file) throws Exception {
        fw = new FileWriter(file, true);
        writer = new BufferedWriter(fw);
    }

    private void saveFile() throws Exception {
        writer.flush();
        writer.close();
    }

    private void fileWait(File file){
        if (file.exists()) {
            do {
            } while (!file.canWrite());
        }
    }

    public void saveSalesRank(String category, int[] salesRank)throws Exception{
        new File("rank").mkdirs();
        file = new File("rank/salesRanks " + (getDate()) + ".txt");
        fileWait(file);

        String writeStr = String.format("%s : %d, %d, %d%n", category, salesRank[0], salesRank[1], salesRank[2]);

        createFile(file);
        writer.append(writeStr);
        saveFile();
    }

    public void saveQueryToSQL(String query, String filename) throws Exception{
        new File("save").mkdirs();
        file = new File("save/" + filename + ".sql");
        fileWait(file);

        createFile(file);
        writer.append(query + "\n");
        saveFile();
    }

    public void saveStringToTxt(String string, String filename) throws Exception{
        new File("save").mkdirs();
        file = new File("save/" + filename + ".txt");
        fileWait(file);

        String writeStr = String.format("%s%n", string);

        createFile(file);
        writer.append(writeStr);
        saveFile();
    }

    private String getTimeDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }



}
