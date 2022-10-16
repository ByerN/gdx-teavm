package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.cpp.b2Vec2;
import com.badlogic.gdx.physics.box2d.cpp.b2World;

public class World {

    private Vector2 gravity;
    private b2World world;

    public World (Vector2 gravity, boolean doSleep) {
        this.gravity = new Vector2();
        b2Vec2 b2Gravity = new b2Vec2();
        b2Gravity.x(gravity.x);
        b2Gravity.y(gravity.y);
        world = new b2World(b2Gravity);
    }

    public void dispose() {
        world = null;
    }

    public Vector2 getGravity() {
        b2Vec2 b2Vec2 = world.GetGravity();
        gravity.set(b2Vec2.x(), b2Vec2.y());
        return gravity;
    }

    public void step (float timeStep, int velocityIterations, int positionIterations) {
        world.Step(timeStep, velocityIterations, positionIterations);
    }
}