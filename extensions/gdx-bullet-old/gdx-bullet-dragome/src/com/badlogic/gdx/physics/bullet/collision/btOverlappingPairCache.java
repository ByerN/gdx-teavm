/*-------------------------------------------------------
 * This file was generated by XpeCodeGen
 * Version 1.0.0
 *
 * Do not make changes to this file
 *-------------------------------------------------------*/
package com.badlogic.gdx.physics.bullet.collision;

/**
 * @author xpenatan
 */
public class btOverlappingPairCache extends btOverlappingPairCallback {

    btBroadphasePairArray tmp = new btBroadphasePairArray(0, false);

    public btOverlappingPairCache(long cPtr, boolean cMemoryOwn) {
        super(cPtr, cMemoryOwn);
    }

    protected void cacheObj() {
        com.dragome.commons.javascript.ScriptHelper.put("addr", this.cPointer, this);
        this.jsObj = com.dragome.commons.javascript.ScriptHelper.eval("Bullet.wrapPointer(addr,Bullet.btOverlappingPairCache);", this);
    }

    public btBroadphasePairArray getOverlappingPairArray() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        long addr = com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(jsObj.getOverlappingPairArray());", this);
        tmp.resetObj(addr, false);
        return tmp;
    }
}
