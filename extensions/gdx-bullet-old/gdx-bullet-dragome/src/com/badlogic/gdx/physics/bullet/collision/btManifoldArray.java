/*-------------------------------------------------------
 * This file was generated by XpeCodeGen
 * Version 1.0.0
 *
 * Do not make changes to this file
 *-------------------------------------------------------*/
package com.badlogic.gdx.physics.bullet.collision;

import com.badlogic.gdx.physics.bullet.BulletBase;

/**
 * @author xpenatan
 */
public class btManifoldArray extends BulletBase {

    btPersistentManifold tmp = new btPersistentManifold(0, false);

    public btManifoldArray() {
        resetObj(createNative(), true);
    }

    private static long createNative() {
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("var cobj=new Bullet.btManifoldArray();", null);
        return com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(cobj);", null);
    }

    public btManifoldArray(long cPtr, boolean cMemoryOwn) {
        resetObj(cPtr, cMemoryOwn);
    }

    protected void cacheObj() {
        com.dragome.commons.javascript.ScriptHelper.put("addr", this.cPointer, this);
        this.jsObj = com.dragome.commons.javascript.ScriptHelper.eval("Bullet.wrapPointer(addr,Bullet.btManifoldArray);", this);
    }

    public int size() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        return com.dragome.commons.javascript.ScriptHelper.evalInt("jsObj.size();", this);
    }

    /**
     * Dont keep a reference of any object.
     */
    public btPersistentManifold at(int n) {
        com.dragome.commons.javascript.ScriptHelper.put("n", n, this);
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        long addr = com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(jsObj.at(n));", this);
        tmp.resetObj(addr, false);
        return tmp;
    }

    public int capacity() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        return com.dragome.commons.javascript.ScriptHelper.evalInt("jsObj.capacity();", this);
    }

    public void resize(int newsize) {
        com.dragome.commons.javascript.ScriptHelper.put("newsize", newsize, this);
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.resize(newsize);", this);
    }
}
