package com.walker.study.proxy;

import android.util.Log;

public class Cat implements IAnimal,IClimb {
    @Override
    public void eat() {
        Log.i("IAnimal","love eating fish !");

    }

    @Override
    public void climb() {
        Log.i("IClimb","love climbing tree !");
    }
}
