package it.aulab.progetto_finale_aliceaccillaro.utils;

public class StringManipulation {

    public static String getFileExtension(String fileName) {

        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }
}