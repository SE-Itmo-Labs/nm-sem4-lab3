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
    protected double xStart;

    @NotNull
    @NotEmpty
    @Description(
            name = "xEnd",
            description = "End x (правая граница интервала)"
    )
    protected double xEnd;

    @NotNull
    @NotEmpty
    @PositiveNumber
    @Description(
            name = "epsilon",
            description = "Точность (пожалуйста, укажите до 0.001)"
    )
    protected long epsilon;

    public Lab3Form(double xStart, double xEnd, long epsilon) {
        setXStart(xStart);
        setXEnd(xEnd);
        setEpsilon(epsilon);
    }

    public Lab3Form() {
        this(0,0,0);
    }

    @Override
    public int compareTo(Lab3Form object) {
        return 0;
    }

    @Override
    public Lab3Form random() {
        return null;
    }
}
