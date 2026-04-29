/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.collection.model;

import com.ssnagin.collectionmanager.description.annotations.Description;
import com.ssnagin.collectionmanager.object.Entity;
import com.ssnagin.collectionmanager.validation.annotations.MaxValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author DEVELOPER
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Coordinates extends Entity<Coordinates> {
    @MaxValue(value = 180)
    @Description(
            name = "координата х",
            description = "какое-то описание"
    )
    protected Long x = null; //Максимальное значение поля: 180. (Так как long может быть null, ставим Long вместо long)

    @MaxValue(value = 750)
    @Description(
            name = "координата y",
            description = "какое-то описание"
    )
    protected Integer y; //Максимальное значение поля: 750, Поле не может быть null

    public Coordinates(Long x, Integer y) {
        setX(x);
        setY(y);
    }

    public Coordinates(Integer y) {
        this(null, y);
    }

    public Coordinates() {
        this(null, null);
    }

    @Override
    public String toString() {
        return "Coordinates="
                + "{x=" + Long.toString(x) +
                ", y=" + Integer.toString(y) + "}";
    }

    @Override
    public int compareTo(Coordinates otherCoordinates) {
        int result = this.x.compareTo(otherCoordinates.x);

        if (result == 0) result = this.y.compareTo(otherCoordinates.y);

        return result;
    }

    @Override
    public Coordinates random() {
        setX(random.nextLong(Integer.MIN_VALUE, 180));
        setY(random.nextInt(Integer.MIN_VALUE, 750));
        return this;
    }
}
