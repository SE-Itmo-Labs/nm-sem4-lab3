/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.object;

import com.ssnagin.collectionmanager.collection.interfaces.Describable;
import com.ssnagin.collectionmanager.collection.interfaces.Randomize;
import com.ssnagin.collectionmanager.console.Console;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing an entity in the collection management system.
 * Provides common functionality for description generation, comparison, and randomization.
 *
 * @param <T> the type of entity this class will be compared to
 * @author developer
 * @version 1.0
 * @since 2023
 */
@EqualsAndHashCode
public abstract class Entity<T> implements Describable, Comparable<T>, Randomize<T>, Serializable {

    /**
     * Compares this entity with another entity of the same type.
     *
     * @param object the entity to be compared
     * @return a negative integer, zero, or a positive integer as this entity
     * is less than, equal to, or greater than the specified entity
     */
    @Override
    public abstract int compareTo(T object);

    /**
     * Generates a description of the entity using reflection to examine fields and methods.
     * Uses a default depth of 1 for nested entity descriptions.
     *
     * @return a formatted string describing the entity and its fields
     */
    @Override
    public String getDescription() {
        return getDescription(1);
    }

    /**
     * Generates a description of the entity with specified depth for nested entities.
     *
     * @param depth the indentation level for nested entity descriptions
     * @return a formatted string describing the entity and its fields
     */
    public String getDescription(int depth) {
        List<Field> fields = getAllFields(this.getClass());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Entity of ")
                .append(this.getClass().getSimpleName())
                .append(":\n");

        for (Field field : fields) {
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            if (Entity.class.isAssignableFrom(field.getType())) {
                try {
                    Method getter = this.getClass().getMethod(getterName);

                    if (!isGetter(getter)) continue;

                    Entity value = (Entity) getter.invoke(this);
                    stringBuilder.append(getIndent(depth))
                            .append(fieldName)
                            .append(": ")
                            .append(value.getDescription(depth + 1))
                            .append("\n");
                    continue;
                } catch (IllegalArgumentException | IllegalAccessException |
                         InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Console.error(ex.getMessage());
                }
            }

            try {
                Method getter = this.getClass().getMethod(getterName);

                if (isGetter(getter)) {
                    Object value = getter.invoke(this);
                    stringBuilder.append(getIndent(depth))
                            .append(fieldName)
                            .append(": ")
                            .append(value)
                            .append("\n");
                }

            } catch (NoSuchMethodException | SecurityException |
                     IllegalAccessException | InvocationTargetException ex) {
                Console.error(ex.toString());
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Retrieves all fields of a class including those from superclasses.
     *
     * @param clazz the class to inspect
     * @return a list of all fields in the class hierarchy
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    /**
     * Generates an indentation string based on the specified depth.
     *
     * @param indent the number of levels to indent
     * @return a string of spaces representing the indentation
     */
    private static String getIndent(int indent) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indent - 1; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    /**
     * Determines if a method is a getter method.
     *
     * @param method the method to check
     * @return true if the method is a getter, false otherwise
     */
    private static boolean isGetter(Method method) {
        return (method.getName().startsWith("get") &&
                method.getParameterCount() == 0 &&
                !method.getReturnType().equals(void.class));
    }

    /**
     * Determines if a method is a setter method.
     *
     * @param method the method to check
     * @return true if the method is a setter, false otherwise
     */
    private static boolean isSetter(Method method) {
        return (method.getName().startsWith("set") &&
                method.getParameterCount() == 1 &&
                method.getReturnType().equals(void.class));
    }
}