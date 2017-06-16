package com.sujitha.evariant.parsers;

import com.opencsv.CSVReader;
import com.sujitha.evariant.exception.AppException;
import com.sujitha.evariant.utils.FileLoader;
import com.sujitha.evariant.utils.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sneti on 6/14/17.
 */
public class MSAParser {

    final static Logger logger = Logger.getLogger(MSAParser.class);

    final int MSA_NAME = 0;

    final int POPULATION = 2;

    final int MSA_NAME_IN_DILINEATION = 3;

    final int COUNTY = 7;

    String MSA_POPULATION_STATS_FILE_NAME_KEY = "msa.population.stats.file.name";

    String MSA_TO_COUNTIES_FILE_NAME_KEY = "msa.to.counties.file.name";

    /**
     * This method reads the file - data/MSA_population_stats.csv and prepares MSA-Population map
     *
     * @return
     */
    public Map<String, Integer> getMSAPopulationMap() {
        Map<String, Integer> populationMap = new HashMap<>();
        String fileName = null;
        try {
            fileName = PropertyReader.readProperty(MSA_POPULATION_STATS_FILE_NAME_KEY);
            CSVReader reader = FileLoader.loadCsvReader(fileName);
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                if (isValidMsaPopulationRecord(nextLine)) {
                    //Here the population is based on the year 2010. Not sure if we need to derive the Year 2015 population by adding expected growth percentage.
                    // We can use this formula to derive 2015 population -> (2010 Population + GrowthPercentage*2010 Population)
                    populationMap.put(nextLine[MSA_NAME], Integer.parseInt(nextLine[POPULATION].replaceAll(",", "")));
                }
            }
        } catch (IOException e) {
            final String errorMsg = "Exception occurred while reading file: " + fileName;
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        } catch (Exception e) {
            final String errorMsg = "Exception occurred while building the map of MSA - Total population. ";
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        }
        return populationMap;

    }

    private boolean isValidMsaPopulationRecord(String[] nextLine) {
        return nextLine.length >= 2
            && StringUtils.isNotEmpty(nextLine[MSA_NAME])
            && !"".equals(nextLine[POPULATION].trim());
    }

    /**
     * This method read the file - data/msa_to_counties.csv and prepares a Map of MSA - List Of Counties.
     *
     * @return
     */
    public Map<String, List<String>> getMSAtoCountiesMap() {
        Map<String, List<String>> msaToCountiesMap = new HashMap<>();
        String fileName = null;
        try {
            fileName = PropertyReader.readProperty(MSA_TO_COUNTIES_FILE_NAME_KEY);
            CSVReader reader = FileLoader.loadCsvReader(fileName);
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 7) {
                    String msa = nextLine[MSA_NAME_IN_DILINEATION].trim();
                    String county = nextLine[COUNTY].trim().substring(0, nextLine[COUNTY].lastIndexOf(" "));
                    String state = msa.substring(msa.length() - 2);
                    county = (county + "," + state).toUpperCase();
                    List<String> counties;
                    if (msaToCountiesMap.containsKey(msa)) {
                        counties = msaToCountiesMap.get(msa);
                    } else {
                        counties = new ArrayList<>();
                        msaToCountiesMap.put(msa, counties);
                    }
                    counties.add(county);
                }
            }
        } catch (IOException e) {
            final String errorMsg = "Exception occurred while reading file: " + fileName;
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        } catch (Exception e) {
            final String errorMsg = "Exception occurred while building the map of MSA - List Of Counties. ";
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        }
        return msaToCountiesMap;
    }

}
