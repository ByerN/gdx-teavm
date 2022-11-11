/*-------------------------------------------------------
 * This file was generated by XpeCodeGen
 * Version 1.0.0
 *
 * Do not make changes to this file
 *-------------------------------------------------------*/
package com.badlogic.gdx.physics.bullet.linearmath;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.physics.bullet.BulletBase;

/**
 * @author xpenatan
 */
public class btMatrix3x3 extends BulletBase {

    public btMatrix3x3() {
        resetObj(createNative(), true);
    }

    private static long createNative() {
        return com.dragome.commons.javascript.ScriptHelper.evalLong("Bullet.getPointer(new Bullet.btMatrix3x3());", null);
    }

    protected void cacheObj() {
        com.dragome.commons.javascript.ScriptHelper.put("addr", this.cPointer, this);
        this.jsObj = com.dragome.commons.javascript.ScriptHelper.eval("Bullet.wrapPointer(addr,Bullet.btMatrix3x3);", this);
    }

    public static void set(btMatrix3x3 mat, float[] value) {
        com.dragome.commons.javascript.ScriptHelper.put("value", value, null);
        mat.checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", mat.jsObj, null);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.setValue(value[0],value[3],value[6],value[1],value[4],value[7],value[2],value[5],value[8]);", null);
    }

    public static void get(btMatrix3x3 mat, float[] value) {
        com.dragome.commons.javascript.ScriptHelper.put("value", value, null);
        mat.checkPointer();
        Object mat3 = mat.jsObj;
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("value[0]=mat3.getColumn(0).getX();value[1]=mat3.getColumn(0).getY();value[2]=mat3.getColumn(0).getZ();value[3]=mat3.getColumn(1).getX();value[4]=mat3.getColumn(1).getY();value[5]=mat3.getColumn(1).getZ();value[6]=mat3.getColumn(2).getX();value[7]=mat3.getColumn(2).getY();value[8]=mat3.getColumn(2).getZ();", null);
    }

    public static void set(Matrix3 inn, btMatrix3x3 out) {
        out.checkPointer();
        btMatrix3x3.set(out, inn.val);
    }

    public static void get(btMatrix3x3 inn, Matrix3 out) {
        inn.checkPointer();
        btMatrix3x3.get(inn, out.val);
    }
}
