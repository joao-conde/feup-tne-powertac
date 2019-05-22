package org.powertac.brobroker.utils;

import org.powertac.util.Pair;

import java.util.ArrayList;

public class MaxDifference {

    public static Pair<Integer, Integer> maxDiff(ArrayList<Double> arr)
    {
        double max_diff = arr.get(1) - arr.get(0);
        int i, j;
        int minIndex = 0;
        int maxIndex = 0;
        for (i = 0; i < arr.size(); i++)
        {
            for (j = i + 1; j < arr.size(); j++)
            {
                if (arr.get(j) - arr.get(i) > max_diff) {
                    max_diff = arr.get(j) - arr.get(i);
                    minIndex = i;
                    maxIndex = j;
                }

            }
        }
        return new Pair<Integer,Integer>(minIndex, maxIndex);
    }
}
