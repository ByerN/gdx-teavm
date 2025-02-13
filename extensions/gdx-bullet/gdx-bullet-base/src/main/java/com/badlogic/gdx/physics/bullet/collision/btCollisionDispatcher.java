package com.badlogic.gdx.physics.bullet.collision;

/**
 * @author xpenatan
 */
public class btCollisionDispatcher extends btDispatcher {

    /*[-C++;-NATIVE]
        #include "btBulletCollisionCommon.h"
    */

    public btCollisionDispatcher(btCollisionConfiguration config) {
        initObject(createNative(config.getCPointer()), true);
    }

    /*[-C++;-NATIVE]
        btCollisionConfiguration * conf = (btCollisionConfiguration *)addr;
        return (jlong)new btCollisionDispatcher(conf);
    */
    /*[-teaVM;-NATIVE]
        var otherJSObj = Bullet.wrapPointer(addr, Bullet.btCollisionConfiguration);
        var jsObj = new Bullet.btCollisionDispatcher(otherJSObj);
        return Bullet.getPointer(jsObj);
    */
    private static native long createNative(long addr);

    @Override
    protected void deleteNative() {
        deleteNative(cPointer);
    }

    /*[-C++;-NATIVE]
        delete (btCollisionDispatcher*)addr;
    */
    /*[-teaVM;-NATIVE]
        var jsObj = Bullet.wrapPointer(addr, Bullet.btCollisionDispatcher);
        Bullet.destroy(jsObj);
    */
    private static native void deleteNative(long addr);
}