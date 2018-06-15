package com.urise.webapp;

import java.io.File;
import java.io.IOException;

public class MainFile {

    public static void main(String[] args) {

        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/urise/webapp");
        printFileNames(dir, "\t");
    }

    public static void printFileNames(File dir, String paragraph) {
        if (dir.isDirectory()) {
            System.out.println(paragraph + "|_" + dir.getName());
            File[] list = dir.listFiles();
            paragraph = paragraph + "\t";
            if (list != null) {
                for (File file : list) {
                    printFileNames(file, paragraph);
                }
            }
        } else {
            System.out.println(paragraph + dir.getName());
        }
    }


}
