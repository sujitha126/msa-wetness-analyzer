package com.sujitha.evariant.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ValueComparatorTest {

    private Comparator<String> valueComparator;

    private Map<String, Double> msaTotalWetness;
    private Map<String, Double> msaTotalWetnessUnsorted;


    @Before
    public void setUp() throws Exception {
        //Setup data
        msaTotalWetnessUnsorted = new HashMap<>();
        msaTotalWetnessUnsorted.put("Abilene", 10.00);
        msaTotalWetnessUnsorted.put("Bangor", 3.35);
        msaTotalWetnessUnsorted.put("Bismarck", 50.45);
        msaTotalWetnessUnsorted.put("Hammond", 40.45);
        msaTotalWetnessUnsorted.put("Austin", 50.55);

        valueComparator = new ValueComparator(msaTotalWetnessUnsorted);
        msaTotalWetness = new TreeMap<>(valueComparator);
    }

    @Test
    public void test_sortMsaTotalWetness() throws Exception {


        msaTotalWetness.putAll(msaTotalWetnessUnsorted);

        //Verify whether the first element the msaTotalWetness has highest wetness

        Iterator<String> iterator = msaTotalWetness.keySet().iterator();

        //Assert the MSA with highest Wetness
        assertEquals("Austin", iterator.next());
        assertEquals("50.55", msaTotalWetness.get("Austin").toString());

        //Assert the MSA with lowest Wetness
        String lowestWetnessArea = null;

        while (iterator.hasNext()) {
            lowestWetnessArea = iterator.next();
        }

        assertEquals("Bangor", lowestWetnessArea);
        assertEquals("3.35", msaTotalWetness.get("Bangor").toString());
    }
}