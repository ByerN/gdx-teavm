package com.badlogic.gdx.physics.bullet.collision;

/**
 * @author xpenatan
 */
public class btDefaultCollisionConfiguration extends btCollisionConfiguration {

    /*[-C++;-NATIVE]
        #include "btBulletCollisionCommon.h"
    */

    public btDefaultCollisionConfiguration() {
        initObject(createNative(), true);
    }

    @Override
    protected void deleteNative() {
        deleteNative(cPointer);
    }

    /*[-C++;-NATIVE]
        return (jlong)new btDefaultCollisionConfiguration();
    */
    /*[-teaVM;-NATIVE]
        var jsObj = new Bullet.btDefaultCollisionConfiguration();
        return Bullet.getPointer(jsObj);
    */
    private static native long createNative();

    /*[-C++;-NATIVE]
        delete (btDefaultCollisionConfiguration*)addr;
    */
    /*[-teaVM;-NATIVE]
        var jsObj = Bullet.wrapPointer(addr, Bullet.btDefaultCollisionConfiguration);
        Bullet.destroy(jsObj);
    */
    private static native void deleteNative(long addr);
}