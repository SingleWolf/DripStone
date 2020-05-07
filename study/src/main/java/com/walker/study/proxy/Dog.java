package com.walker.study.proxy;

import android.util.Log;

public class Dog implements IAnimal {
    @Override
    public void eat() {
        Log.i("IAnimal","love eating bone !");
    }
}
