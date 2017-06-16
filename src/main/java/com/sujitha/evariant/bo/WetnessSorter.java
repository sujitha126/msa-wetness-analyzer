package com.sujitha.evariant.bo;

import com.sujitha.evariant.exception.AppException;
import com.sujitha.evariant.model.WBAN;
import com.sujitha.evariant.parsers.MSAParser;
import com.sujitha.evariant.parsers.PrecipitationParser;
import com.sujitha.evariant.parsers.WBANParser;
import com.sujitha.evariant.utils.ValueComparator;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by sneti on 6/15/17.
 */
public class WetnessSorter {
    private final static Logger logger = Logger.getLogger(WetnessSorter.class);


    public Map<String, Double> readFilesAndSortMSAByWetness() throws AppException {
        Map<String, Integer> msaPopulationMap;
        Map<String, List<String>> msaToCountiesMap;
        Map<String, List<WBAN>> countiesToWBanMap;
        Map<String, Float> wbanPrecipitationMap;
        ExecutorService executorService = null;

        try {
            //Use ExecutorService to submit 4 tasks, this tasks will run simultaneously
            executorService = Executors.newFixedThreadPool(4);

            Callable<Map<String, Float>> wbanPrecipitationReaderTask = () -> new PrecipitationParser().getWBanPrecipitationMapForMay2015();
            Future<Map<String, Float>> wbanPrecipitationReaderFuture = executorService.submit(wbanPrecipitationReaderTask);

            Callable<Map<String, Integer>> msaPopulationReaderTask = () -> new MSAParser().getMSAPopulationMap();
            Future<Map<String, Integer>> msaPopulationReaderFuture = executorService.submit(msaPopulationReaderTask);

            Callable<Map<String, List<String>>> msaToCountiesReaderTask = () -> new MSAParser().getMSAtoCountiesMap();
            Future<Map<String, List<String>>> msaToCountiesReaderFuture = executorService.submit(msaToCountiesReaderTask);

            Callable<Map<String, List<WBAN>>> countiesToWbanReaderTask = () -> new WBANParser().getCountyStatesWBanMap();
            Future<Map<String, List<WBAN>>> countiesToWbanReaderFuture = executorService.submit(countiesToWbanReaderTask);

            // Parse all the data to respective maps. HashMaps are used to allow O(1) complexity for all reads.
            msaPopulationMap = msaPopulationReaderFuture.get();
            msaToCountiesMap = msaToCountiesReaderFuture.get();
            countiesToWBanMap = countiesToWbanReaderFuture.get();
            wbanPrecipitationMap = wbanPrecipitationReaderFuture.get();

            // Calculate Wetness for MSAs.
            Map<String, Double> wetnessMap = calculateWetnessForAllMSAs(msaPopulationMap, msaToCountiesMap, countiesToWBanMap, wbanPrecipitationMap);

            // Sort and return MSAs based on wetness
            Comparator<String> comparator = new ValueComparator<>(wetnessMap);
            TreeMap<String, Double> sortedWetnessMap = new TreeMap<>(comparator);
            sortedWetnessMap.putAll(wetnessMap);
            return  sortedWetnessMap;
        }  catch (AppException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Exception occurred while parsing the data files.";
            logger.error(errorMsg, e);
            throw new AppException(errorMsg, e);
        } finally {
            executorService.shutdownNow();
        }
    }

    private Map<String,Double> calculateWetnessForAllMSAs(Map<String, Integer> msaPopulationMap, Map<String, List<String>> msaToCountiesMap,
        Map<String, List<WBAN>> countiesToWBanMap, Map<String, Float> wBanPrecipitationMap) {

        Map<String, Double> wetnessMap = new HashMap<>();

        for(String msaName: msaPopulationMap.keySet()) {
            Double total_precipitation = 0.0;
            Double msaWetness = 0.0;
            // Get list of counties
            List<String> counties = msaToCountiesMap.get(msaName);
            if (counties != null) {
                for (String county : counties) {
                    List<WBAN> wbans = countiesToWBanMap.get(county);
                    if (wbans != null) {
                        total_precipitation += findWBanTotalPrecipitation(wbans, wBanPrecipitationMap);
                    }
                }
                msaWetness = total_precipitation * msaPopulationMap.get(msaName);
                wetnessMap.put(msaName, msaWetness);
            }
        }
        return  wetnessMap;
    }

    private float findWBanTotalPrecipitation(List<WBAN> wbans, Map<String, Float> wBanPrecipitationMap) {
        float total = 0.0f;
        for(WBAN wban: wbans) {
            String wbanId = wban.getWbanId();
            if(wban!=null && wBanPrecipitationMap.containsKey(wbanId)) {
                total += wBanPrecipitationMap.get(wbanId);
            }
        }
        return total;
    }
}
