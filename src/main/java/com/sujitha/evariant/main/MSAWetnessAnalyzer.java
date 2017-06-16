package com.sujitha.evariant.main;

import com.sujitha.evariant.bo.WetnessSorter;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Main method which calls WetnessSorter to compute
 * Created by sneti on 6/14/17.
 */
public class MSAWetnessAnalyzer {

    final static Logger logger = Logger.getLogger(MSAWetnessAnalyzer.class);


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        try {
            WetnessSorter wetnessSorter = new WetnessSorter();
            Map<String, Double> sortedMsaWetnessMap = wetnessSorter.readFilesAndSortMSAByWetness();

            System.out.println("------------------------------------------------------");
            System.out.println("List of MSAs in the Descending order of their wetness.");
            System.out.println("------------------------------------------------------");

            for(String msa: sortedMsaWetnessMap.keySet()) {
                System.out.println(msa);
            }
            System.out.println("----End Of Results----");

            //Use sortedMsaWetnessMap.entrySet() to generate graphs, Key represents the MSA name, Value represents the wetness.

        } catch (Exception e) {
            logger.error("Exception occurred while building MSA - Wetness details.", e);
            throw e;
        } finally {
            long totalTime = System.currentTimeMillis() - startTime;
            logger.info("Total Time taken (milliseconds): " + totalTime);
        }

    }
}
