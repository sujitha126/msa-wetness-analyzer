package com.sujitha.evariant.bo;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by sneti on 6/16/17.
 */
public class WetnessSorterIT {
    private WetnessSorter wetnessSorter;

    @Before
    public void setUp() throws Exception {
        wetnessSorter = new WetnessSorter();
        //Test Data is setup in the files, We can also consider generating test files

    }

    @Test
    public void readFilesAndSortMSAByWetness() throws Exception {
        Map<String, Double> readFilesAndSortMSAByWetness = wetnessSorter.readFilesAndSortMSAByWetness();

        System.out.println(readFilesAndSortMSAByWetness);
        //Based on Test Data - Austin should be the wettest region and Bakersfield should be second Wettest

        assertNotNull(readFilesAndSortMSAByWetness);

        Iterator<String> iterator = readFilesAndSortMSAByWetness.keySet().iterator();

        String mostWettestMSA = iterator.next();
        assertEquals("Austin-Round Rock, TX", mostWettestMSA);

        String secondWettestMSA = iterator.next();
        assertEquals("Bakersfield, CA", secondWettestMSA);
    }

}