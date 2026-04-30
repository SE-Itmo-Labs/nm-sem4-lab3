package com.ssnagin.collectionmanager.reflection;

import com.ssnagin.collectionmanager.console.Console;
import com.ssnagin.collectionmanager.description.annotations.Description;
import com.ssnagin.collectionmanager.validation.ValidationManager;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Class for Meta-class data manipulation (and more)
 *
 * @author developer
 */
public final class Reflections {

    /**
     * Parses the given model
     *
     * @param <T>
     * @param type
     * @param scanner
     * @return
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T> T parseModel(Class<T> type, Scanner scanner) throws
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {

        // Let's start with constructor:
        T instance = type.getDeclaredConstructor().newInstance();

        for (Field field : type.getDeclaredFields()) {

            if (!field.isAnnotationPresent(Description.class)) continue;

            field.setAccessible(true);

            Description annotation = field.getAnnotation(Description.class);
            Console.log("Введите " + annotation.name() + " (" + annotation.description() + ") " + field.getAnnotatedType());

            Object value = Reflections.parseField(field.getType(), scanner);

            // КОСТЫЛЬ!!!
            ValidationManager.getInstance().validateField(value, field);

            Reflections.setFieldValue(instance, field, value);
        }

        return instance;
    }

    /**
     * Parse field of a collection
     *
     * @param type
     * @param scanner
     * @return
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object parseField(Class<?> type, Scanner scanner) throws
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {

        if (Reflections.isPrimitive(type)) return Reflections.parsePrimitiveInput(type, scanner);

        if (type.isEnum()) return Reflections.parseEnumInput(type, scanner);

        return Reflections.parseModel(type, scanner);
    }

    /**
     * Parse field of a collection
     *
     * @param type
     * @param input
     * @return
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object parseField(Class<?> type, String input) throws
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {

        // So okay, if Class type belongs to primitives, it tries to parse 
        // the same primitive. If it is enum, the same story. 
        // But what if another Class??? I think nothing for now.

        if (Reflections.isPrimitive(type)) return Reflections.parsePrimitiveInput(type, input);

        if (type.isEnum()) return Reflections.parseEnumInput(type, input);

        // КОСТЫЛЬ!!!!
        return null;
    }

    /**
     * Clarifies if the given object's type is primitive
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz);
    }

    public static java.lang.reflect.Method findSetterByParameterName(Class<?> clazz, Class<?> parameterType) {
        for (java.lang.reflect.Method method : clazz.getMethods()) {
            if (method.getName().startsWith("set") &&
                    method.getParameterCount() == 1) {
                Class<?> methodParamType = method.getParameterTypes()[0];

                // Use ClassUtils for correct primitive check (primitive <-> wrapper)
                if (ClassUtils.isAssignable(parameterType, methodParamType)) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * Set the value to selected field
     *
     * @param instance
     * @param value
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setFieldValue(Object instance, Field field, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        // Строим имя сеттера: fieldName -> setFieldName
        String fieldName = field.getName();
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // Ищем метод с точным именем и типом параметра
        Class<?> fieldType = field.getType();
        Class<?> searchType = fieldType.isPrimitive() ? fieldType :
                (fieldType == Double.class ? double.class :
                        fieldType == Integer.class ? int.class :
                                fieldType == Long.class ? long.class :
                                        fieldType == Float.class ? float.class :
                                                fieldType == Boolean.class ? boolean.class : fieldType);

        java.lang.reflect.Method setter = instance.getClass().getMethod(setterName, searchType);
        setter.invoke(instance, value);
    }

    /**
     * Parses string and returns object with type according to the parsedData
     *
     * @param type
     * @param scanner
     * @return
     */
    public static Object parsePrimitiveInput(Class<?> type, Scanner scanner) {

        Console.print("       | ");

        String input = scanner.nextLine().trim();

        return parsePrimitiveInput(type, input);
    }

    /**
     * Parses string and returns object with type according to the parsedData
     *
     * @param type
     * @param input
     * @return
     */
    public static Object parsePrimitiveInput(Class<?> type, String input) {

        // VALIDATION HERE | NoT yEt ImPlEmEnTeD!!!!!
        // ex. validatePrimitiveInput()

        if (type == char[].class) {
            type = String.class;
        }

        // Let's Map our statements in order to simplify our life
        Map<Class<?>, Function<String, Object>> parsers = new HashMap<>();

        parsers.put(int.class, Integer::parseInt);
        parsers.put(Integer.class, Integer::parseInt);
        parsers.put(long.class, Long::parseLong);
        parsers.put(Long.class, Long::parseLong);
        parsers.put(double.class, s -> Double.parseDouble(s.replace(',', '.')));
        parsers.put(Double.class, s -> Double.parseDouble(s.replace(',', '.')));
        parsers.put(float.class, s -> Float.parseFloat(s.replace(',', '.')));
        parsers.put(Float.class, s -> Float.parseFloat(s.replace(',', '.')));
        parsers.put(byte.class, Byte::parseByte);
        parsers.put(Byte.class, Byte::parseByte);
        parsers.put(boolean.class, Boolean::parseBoolean);
        parsers.put(Boolean.class, Boolean::parseBoolean);

        parsers.put(String.class, s -> s);

        Function<String, Object> parser = parsers.get(type);

        return parser.apply(input);
    }

    /**
     * Tries to parse enum type with custom user's input
     *
     * @param type
     * @param scanner
     * @return
     */
    public static Object parseEnumInput(Class<?> type, Scanner scanner) {

        Object[] values = type.getEnumConstants();

        Console.separatePrint("Доступные имена: ", "ENUM");

        for (Object object : values) {
            Console.println("- " + object);
        }

        Console.print("       | ");

        String input = scanner.nextLine().trim();
        return parseEnumInput(type, input);
    }

    /**
     * Tries to parse enum type
     *
     * @param type
     * @param input
     * @return
     */
    public static Object parseEnumInput(Class<?> type, String input) {
        return Enum.valueOf((Class<? extends Enum>) type, input);
    }

    public static <T extends Annotation> T getAnnotation(Class<T> annotation, Field field) {
        if (!field.isAnnotationPresent(annotation)) return null;
        return field.getAnnotation(annotation);
    }
}
