package com.sujitha.evariant.parsers;

import com.opencsv.CSVReader;
import com.sujitha.evariant.exception.AppException;
import com.sujitha.evariant.utils.FileLoader;
import com.sujitha.evariant.utils.PropertyReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sneti on 6/14/17.
 * This class reads data from "data/201505precip.txt" and builds a Map Of WBANs with their total precipitation.
 */
public class PrecipitationParser {

    final static Logger logger = Logger.getLogger(PrecipitationParser.class);

    public static final String PRECIPITATION_FILE_NAME_KEY = "precipitation.file.name";

    //Elements index in the file
    final int WBAN_ID = 0;
    final int DATE = 1;
    final int HOUR = 2;
    final int PRECIPITATION = 3;


    public Map<String, Float> getWBanPrecipitationMapForMay2015() {
        Map<String, Float> wbanPrecipitationMap = new HashMap<>();
        String fileName = PropertyReader.readProperty(PRECIPITATION_FILE_NAME_KEY);
        try {
            CSVReader reader = FileLoader.loadCsvReader(fileName);
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4) {
                    String wbanId = nextLine[WBAN_ID].trim();
                    String date = nextLine[DATE];
                    String hourString = nextLine[HOUR];
                    String precipitationString = nextLine[PRECIPITATION];

                    Float precipitation = null;
                    Integer hour = null;

                    try {
                        if (precipitationString != null && hourString != null) {
                            precipitation = Float.parseFloat(precipitationString);
                            hour = Integer.parseInt(hourString);
                        }
                    } catch (NumberFormatException e) {
                        final String msg = String
                            .format("Invalid precipitation details - '%s' for WBAN_ID: '%s' in file: '%s'.",
                                    precipitationString, wbanId, fileName);
                        logger.debug(msg, e);
                        continue;
                    }

                    if (precipitation != null && hour != null) {
                        // Check if hour is between 7AM to 12PM
                        if (date.contains("201505") && hour >= 8 && hour <= 23) {
                            final float total_precipitation = precipitation + wbanPrecipitationMap.getOrDefault(wbanId, 0.0f);
                            wbanPrecipitationMap.put(wbanId, total_precipitation);
                        }
                    }
                }
            }
        } catch (IOException e) {
            final String errorMsg = "Exception occurred while reading file: " + fileName;
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        } catch (Exception e) {
            final String errorMsg = "Exception occurred while building the map of WBAN ID - Total Precipitation. ";
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        }
        return wbanPrecipitationMap;
    }
}
