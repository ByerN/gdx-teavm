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
public class btGhostObject extends btCollisionObject {

    protected void create() {
        resetObj(createNative(), true);
    }

    private static long createNative() {
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("var cobj=new Bullet.btGhostObject();", null);
        return com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(cobj);", null);
    }

    protected void cacheObj() {
        com.dragome.commons.javascript.ScriptHelper.put("addr", this.cPointer, this);
        this.jsObj = com.dragome.commons.javascript.ScriptHelper.eval("Bullet.wrapPointer(addr,Bullet.btGhostObject);", this);
    }

    public int getNumOverlappingObjects() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        return com.dragome.commons.javascript.ScriptHelper.evalInt("jsObj.getNumOverlappingObjects();", this);
    }

    public btCollisionObject getOverlappingObject(int index, btCollisionWorld world) {
        com.dragome.commons.javascript.ScriptHelper.put("index", index, this);
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        long addr = com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(jsObj.getOverlappingObject(index));", this);
        return world.bodies.get(addr);
    }
}
