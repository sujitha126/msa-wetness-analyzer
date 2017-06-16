package com.sujitha.evariant.utils;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by sneti on 6/14/17.
 */
public class FileLoader {

    public static File readFile(final String fileName) {
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    public static FileReader loadFileReader(final String fileName) throws FileNotFoundException {
        return new FileReader(readFile(fileName));
    }

    public static CSVReader loadCsvReader(final String fileName) throws FileNotFoundException {
        return new CSVReader(loadFileReader(fileName));
    }

}
