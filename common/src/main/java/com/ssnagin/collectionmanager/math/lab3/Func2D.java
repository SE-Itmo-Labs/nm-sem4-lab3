package com.ssnagin.collectionmanager.math.lab3;

public interface Func2D {

    String name();

    double phiSysX(double x, double y);
    double phiSysY(double x, double y);
    double fSys1(double x, double y);
    double fSys2(double x, double y);

    double df1dx(double x, double y);
    double df1dy(double x, double y);
    double df2dx(double x, double y);
    double df2dy(double x, double y);
} 