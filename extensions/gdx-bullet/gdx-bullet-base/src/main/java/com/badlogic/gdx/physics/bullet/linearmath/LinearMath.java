package com.badlogic.gdx.physics.bullet.linearmath;

/**
 * @author xpenatan
 */
public class LinearMath {

    /*[-C++;-NATIVE]
        #include "LinearMath/btScalar.h"
    */

    /* [-C++;-NATIVE]
        return btGetVersion();
    */
    /* [-teaVM;-NATIVE]
        return Bullet.MyClassHelper.prototype.getBTVersion();
    */
    public static native int btGetVersion();
}