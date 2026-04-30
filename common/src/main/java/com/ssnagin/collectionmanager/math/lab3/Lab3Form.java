package com.ssnagin.collectionmanager.math.lab3;

import com.ssnagin.collectionmanager.description.annotations.Description;
import com.ssnagin.collectionmanager.object.Entity;
import com.ssnagin.collectionmanager.validation.annotations.NotEmpty;
import com.ssnagin.collectionmanager.validation.annotations.NotNull;
import com.ssnagin.collectionmanager.validation.annotations.PositiveNumber;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Lab3Form extends Entity<Lab3Form> {

    @NotNull
    @NotEmpty
    @Description(
            name = "xStart",
            description = "Start x (левая граница интервала)"
    )
    public Double xStart;

    @NotNull
    @NotEmpty
    @Description(
            name = "xEnd",
            description = "End x (правая граница интервала)"
    )
    public Double xEnd;

    @NotNull
    @NotEmpty
    @PositiveNumber
    @Description(
            name = "epsilon",
            description = "Точность (пожалуйста, укажите до 0.001)"
    )
    public Double epsilon;

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setXStart(double xStart) {
        this.xStart = xStart;
    }

    public void setXEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public Lab3Form(Double xStart, Double xEnd, Double epsilon) {
        setXStart(xStart);
        setXEnd(xEnd);
        setEpsilon(epsilon);
    }

    public Lab3Form() {
        this(0.0,0.0,0.0);
    }

    @Override
    public int compareTo(Lab3Form object) {
        return 0;
    }

    @Override
    public Lab3Form random() {
        return null;
    }

    @Override
    public String toString() {
        return "Lab3Form{xStart=" + xStart + ", xEnd=" + xEnd + ", epsilon=" + epsilon + "}";
    }
}
