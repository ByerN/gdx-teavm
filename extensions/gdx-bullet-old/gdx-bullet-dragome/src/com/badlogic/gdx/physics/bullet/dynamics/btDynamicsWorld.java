/*-------------------------------------------------------
 * This file was generated by XpeCodeGen
 * Version 1.0.0
 *
 * Do not make changes to this file
 *-------------------------------------------------------*/
package com.badlogic.gdx.physics.bullet.dynamics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;

/**
 * @author xpenatan
 */
public class btDynamicsWorld extends btCollisionWorld {

    btConstraintSolver constraintSolver;

    public int stepSimulation(float timeStep, int maxSubSteps, float fixedTimeStep) {
        return stepSimulation(cPointer, timeStep, maxSubSteps, fixedTimeStep);
    }

    public int stepSimulation(float timeStep, int maxSubSteps) {
        return stepSimulation(cPointer, timeStep, maxSubSteps, 1.0f / 60.0f);
    }

    public int stepSimulation(float timeStep) {
        return stepSimulation(cPointer, timeStep, 1, 1.0f / 60.0f);
    }

    private int stepSimulation(long addr, float timeStep, int maxSubSteps, float fixedTimeStep) {
        com.dragome.commons.javascript.ScriptHelper.put("fixedTimeStep", fixedTimeStep, this);
        com.dragome.commons.javascript.ScriptHelper.put("maxSubSteps", maxSubSteps, this);
        com.dragome.commons.javascript.ScriptHelper.put("timeStep", timeStep, this);
        com.dragome.commons.javascript.ScriptHelper.put("addr", addr, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        return com.dragome.commons.javascript.ScriptHelper.evalInt("jsObj.stepSimulation(timeStep,maxSubSteps,fixedTimeStep);", this);
    }

    public void addConstraint(btTypedConstraint constraint, boolean disableCollisionsBetweenLinkedBodies) {
        addConstraint(cPointer, constraint.cPointer, disableCollisionsBetweenLinkedBodies);
    }

    public void addConstraint(btTypedConstraint constraint) {
        addConstraint(cPointer, constraint.cPointer, false);
    }

    private void addConstraint(long addr, long typedConstraintAddr, boolean disableCollisionsBetweenLinkedBodies) {
        com.dragome.commons.javascript.ScriptHelper.put("disableCollisionsBetweenLinkedBodies", disableCollisionsBetweenLinkedBodies, this);
        com.dragome.commons.javascript.ScriptHelper.put("typedConstraintAddr", typedConstraintAddr, this);
        com.dragome.commons.javascript.ScriptHelper.put("addr", addr, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("var typedCon=Bullet.wrapPointer(typedConstraintAddr,Bullet.btTypedConstraint);jsObj.addConstraint(typedCon,disableCollisionsBetweenLinkedBodies);", this);
    }

    public void removeConstraint(btTypedConstraint constraint) {
        removeConstraint(cPointer, constraint.cPointer);
    }

    private void removeConstraint(long addr, long typedConstraintAddr) {
        com.dragome.commons.javascript.ScriptHelper.put("typedConstraintAddr", typedConstraintAddr, this);
        com.dragome.commons.javascript.ScriptHelper.put("addr", addr, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("var typedCon=Bullet.wrapPointer(typedConstraintAddr,Bullet.btTypedConstraint);jsObj.removeConstraint(typedCon);", this);
    }

    public void addAction(btActionInterface action) {
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsAct", action.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.addAction(jsAct);", this);
    }

    public void removeAction(btActionInterface action) {
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsAct", action.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.removeAction(jsAct);", this);
    }

    public void setGravity(Vector3 gravity) {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("x", gravity.x, this);
        com.dragome.commons.javascript.ScriptHelper.put("y", gravity.y, this);
        com.dragome.commons.javascript.ScriptHelper.put("z", gravity.z, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.setGravity(Bullet.MyTemp.prototype.btVec3_1(x,y,z));", this);
    }

    public void setGravity(float x, float y, float z) {
        com.dragome.commons.javascript.ScriptHelper.put("z", z, this);
        com.dragome.commons.javascript.ScriptHelper.put("y", y, this);
        com.dragome.commons.javascript.ScriptHelper.put("x", x, this);
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.setGravity(Bullet.MyTemp.prototype.btVec3_1(x,y,z));", this);
    }

    public void getGravity(Vector3 out) {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("var gravity=jsObj.getGravity();", this);
        out.x = com.dragome.commons.javascript.ScriptHelper.evalFloat("gravity.x();", this);
        out.y = com.dragome.commons.javascript.ScriptHelper.evalFloat("gravity.y();", this);
        out.z = com.dragome.commons.javascript.ScriptHelper.evalFloat("gravity.z();", this);
    }

    public void addRigidBody(btRigidBody body) {
        checkPointer();
        addBody(body);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsBod", body.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.addRigidBody(jsBod);", this);
    }

    public void addRigidBody(btRigidBody body, short group, short mask) {
        checkPointer();
        addBody(body);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsBod", body.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.addRigidBody(jsBod,group,mask);", this);
    }

    public void removeRigidBody(btRigidBody body) {
        checkPointer();
        removeBody(body);
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsBod", body.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.removeRigidBody(jsBod);", this);
    }

    public void setConstraintSolver(btConstraintSolver solver) {
        checkPointer();
        this.constraintSolver = solver;
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.put("jsSol", solver.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.setConstraintSolver(jsSol);", this);
    }

    public btConstraintSolver getConstraintSolver() {
        return constraintSolver;
    }

    public int getNumConstraints() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        return com.dragome.commons.javascript.ScriptHelper.evalInt("jsObj.getNumConstraints();", this);
    }

    public void clearForces() {
        checkPointer();
        com.dragome.commons.javascript.ScriptHelper.put("jsObj", this.jsObj, this);
        com.dragome.commons.javascript.ScriptHelper.evalNoResult("jsObj.clearForces();", this);
    }
}
