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

        printFileNames(dir);
    }

    public static void printFileNames(File dir) {
        if (dir.isDirectory()) {
            String[] list = dir.list();
            for (String name : list) {
                File file = null;
                try {
                    file = new File(dir.getCanonicalPath() + "/" + name);
                    printFileNames(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else System.out.println(dir.getName());
    }


}
