package com.badlogic.gdx.physics.box2d.cpp;

import com.badlogic.gdx.physics.box2d.Box2DBase;

public class b2Vec2 extends Box2DBase {

    public static final b2Vec2 TMP_01 = new b2Vec2();
    public static final b2Vec2 TMP_02 = new b2Vec2();

    public b2Vec2() {
        initObject(createNative(), true);
    }

    public b2Vec2(boolean cMemoryOwn) {
        initObject(0, false);
    }

    /*[-teaVM;-NATIVE]
        var jsObj = new Box2D.b2Vec2();
        return Box2D.getPointer(jsObj);
    */
    private static native long createNative();

    public native void Set(float x, float y);

    public native float x();

    public native float y();

    public native void x(float value);

    public native void y(float value);
}