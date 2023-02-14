package org.um.feri.ears.util.annotation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class AnnotationUtil {

    private AnnotationUtil(){}

    public static String getParameterNamesAndValues(Object object) {
        String parametersString = "";
        try {
            Class<?> objectClass = requireNonNull(object).getClass();
            Map<String, String> parametersMap = new HashMap<>();

            for (Field field: objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(AlgorithmParameter.class)) {
                    parametersMap.put(getParameterDescription(field), field.get(object).toString());
                }
            }

            parametersString = parametersMap.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + " : " + entry.getValue())
                    .collect(Collectors.joining(", "));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return parametersString;
    }

    public static String getParameterValue(Object object, String parameterName) {
        try {
            Class<?> objectClass = requireNonNull(object).getClass();
            for (Field field: objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(AlgorithmParameter.class)) {
                    if(field.getName().equals(parameterName) || field.getAnnotation(AlgorithmParameter.class).name().equals(parameterName)) {
                        return field.get(object).toString();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String getParameterDescription(Field field) {
        String annotationValue = field.getAnnotation(AlgorithmParameter.class).name();

        if (annotationValue.isEmpty()) {
            return field.getName();
        }
        else {
            return annotationValue;
        }
    }
}
