package com.sujitha.evariant.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sneti on 6/15/17.
 * This Comparator is used for sorting a map based on their values
 */
public class ValueComparator<K, V extends Comparable<V>> implements Comparator<K>{

    Map<K, V> map = new HashMap<>();

    public ValueComparator(Map<K, V> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(K s1, K s2) {
        //descending order
        return -map.get(s1).compareTo(map.get(s2));
    }

}