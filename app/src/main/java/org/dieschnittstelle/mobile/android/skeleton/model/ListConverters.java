package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.room.TypeConverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverters {
    private static final String SEPARATOR = ";";

    @TypeConverter
    public static String makeStringfromListOfStrings(List<String> stringList) {
        if (stringList.isEmpty()) {
            return null;
        }
        return stringList.stream().distinct().collect(Collectors.joining(SEPARATOR));
    }

    @TypeConverter
    public static List<String> makeListOfStringsFromString(String string) {
        if (string == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(string.split(SEPARATOR))
                .collect(Collectors.toList());
    }
}
