package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.cpp.b2Fixture;
import com.badlogic.gdx.physics.box2d.cpp.b2Shape;
import com.badlogic.gdx.physics.box2d.cpp.b2Vec2;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Fixture {

    private World world;
    protected b2Fixture b2Fixture;
    protected Shape<?> shape;
    protected Object userData;
    private final Filter filter = new Filter();

    private boolean dirtyFilter = true;

    protected Fixture(World world, long addr) {
        this.world = world;
        b2Fixture = new b2Fixture();
        b2Fixture.setPointer(addr);
    }

    protected void reset(long addr) {
        b2Fixture.setPointer(addr);
        this.shape = null;
        this.userData = null;
        this.dirtyFilter = true;
    }

    /**
     * Sets custom user data.
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Shape.Type getType() {
        int type = b2Fixture.getType();
        switch(type) {
            case 0:
                return Shape.Type.Circle;
            case 1:
                return Shape.Type.Edge;
            case 2:
                return Shape.Type.Polygon;
            case 3:
                return Shape.Type.Chain;
            default:
                throw new GdxRuntimeException("Unknown shape type!");
        }
    }

    public Shape getShape() {
        if(shape == null) {
            b2Shape b2Shape = b2Fixture.GetShape();
            //TODO remove int cast
            int shapeAddr = (int)b2Shape.getCPointer();
            if(shapeAddr == 0) throw new GdxRuntimeException("Null shape address!");
            int type = b2Shape.getType();

            switch(type) {
                case 0:
                    shape = new CircleShape(shapeAddr);
                    break;
                case 1:
                    shape = new EdgeShape(shapeAddr);
                    break;
                case 2:
                    shape = new PolygonShape(shapeAddr);
                    break;
                case 3:
                    shape = new ChainShape(shapeAddr);
                    break;
                default:
                    throw new GdxRuntimeException("Unknown shape type!");
            }
        }

        return shape;
    }

    public Body getBody() {
        long body = b2Fixture.getBody();
        return world.bodies.get(body);
    }

    public boolean testPoint(float x, float y) {
        b2Vec2.TMP_01.Set(x, y);
        return b2Fixture.TestPoint(b2Vec2.TMP_01);
    }
}
