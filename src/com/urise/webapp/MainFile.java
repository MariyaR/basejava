package com.urise.webapp;

import java.io.File;
import java.io.IOException;

public class MainFile {
    private static int i=0;

    public static void main(String[] args) {

        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/urise/webapp");
        printFileNames(dir);
    }

    public static void printFileNames(File dir) {
        if (dir.isDirectory()) {
            for (int j=0;j<i;j++) {
                System.out.print("\t");
            }
            System.out.println("|_" + dir.getName());
            File[] list = dir.listFiles();
            i++;
            if (list != null) {
                for (File file : list) {
                    printFileNames(file);
                }
            }
            i--;
        } else {
            for (int j=0;j<i;j++) {
                System.out.print("\t");
            }
            System.out.println(dir.getName());
        }
    }


}
