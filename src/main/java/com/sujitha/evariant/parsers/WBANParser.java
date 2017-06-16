package com.sujitha.evariant.parsers;

import com.opencsv.CSVReader;
import com.sujitha.evariant.exception.AppException;
import com.sujitha.evariant.model.WBAN;
import com.sujitha.evariant.utils.FileLoader;
import com.sujitha.evariant.utils.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sneti on 6/14/17.
 */
public class WBANParser {

    final static Logger logger = Logger.getLogger(WBANParser.class);


    final int WBAN_ID = 1;
    final int STATION_NAME = 2;
    final int STATE = 3;
    final int COUNTY = 4;

    String WBAN_FILE_NAME_KEY = "wban.file.name";

    /**
     * This method reads the file - data/wbanmasterlist.csv and prepares COUNTY,STATE - List Of WBANs
     * Since Counties are not unique, we are using the key as COUNTY + "," + STATE
     * @return
     */
    public Map<String, List<WBAN>> getCountyStatesWBanMap() {
        Map<String, List<WBAN>> countiesToWbanMap = new HashMap<>();
        String fileName = null;
        try {
            fileName = PropertyReader.readProperty(WBAN_FILE_NAME_KEY);
            CSVReader reader = FileLoader.loadCsvReader(fileName);
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 1) {
                    String stationDetails = nextLine[0].replaceAll("\"", "");
                    String[] wbanDetails = stationDetails.split("\\|");

                    if (isValidWban(wbanDetails)) {
                        String wbanId = wbanDetails[WBAN_ID];
                        String stationName = wbanDetails[STATION_NAME];
                        String state = wbanDetails[STATE];
                        String county = wbanDetails[COUNTY];

                        String countyStates = String.format("%s,%s",county, state).toUpperCase();

                        final WBAN wban = new WBAN(wbanId, stationName, state, county);
                        List<WBAN> wbans;

                        if (countiesToWbanMap.containsKey(countyStates)) {
                            wbans = countiesToWbanMap.get(countyStates);
                        } else {
                            wbans = new ArrayList<>();
                            countiesToWbanMap.put(countyStates, wbans);
                        }
                        wbans.add(wban);
                    }
                }
            }
        } catch (IOException e) {
            final String errorMsg = "Exception occurred while reading file: " + fileName;
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        } catch (Exception e) {
            final String errorMsg = "Exception occurred while building the map of COUNTY,STATE - List Of WBANs ";
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        }
        return countiesToWbanMap;

    }

    private boolean isValidWban(String[] wbanDetails) {
        return wbanDetails.length >= 5
            && StringUtils.isNotEmpty(wbanDetails[COUNTY])
            && StringUtils.isNotEmpty(wbanDetails[STATE]);
    }

}
