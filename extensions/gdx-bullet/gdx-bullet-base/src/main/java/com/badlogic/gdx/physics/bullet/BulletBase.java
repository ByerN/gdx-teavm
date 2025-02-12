package com.badlogic.gdx.physics.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author xpenatan
 */
public abstract class BulletBase implements Disposable {

    public static final float [] FLOAT_4 = new float[4];

    protected long cPointer;
    protected boolean cMemOwn;
    private boolean disposed;
    protected boolean destroyed;
    public String className;
    private int refCount;

    public BulletBase() {
    }

    protected void initObject(long cPtr, boolean cMemoryOwn) {
        cMemOwn = cMemoryOwn;
        cPointer = cPtr;
    }

    /**
     * Set pointer if it's not owned by this object. Useful for setting temp objets
     */
    public void setPointer(long cPtr) {
        if(!cMemOwn) {
            cPointer = cPtr;
        }
    }

    /**
     * Obtains a reference to this object, call release to free the reference.
     */
    public void obtain() {
        refCount++;
    }

    /**
     * Release a previously obtained reference, causing the object to be disposed when this was the last reference.
     */
    public void release() {
        if(--refCount <= 0 && Bullet.useRefCounting) dispose();
    }

    /**
     * @return Whether this instance is obtained using the {@link #obtain()} method.
     */
    public boolean isObtained() {
        return refCount > 0;
    }

    protected void construct() {
        destroyed = false;
    }

    protected void reset(long cPtr, boolean cMemoryOwn) {
        if(!destroyed) destroy();
        cMemOwn = cMemoryOwn;
        cPointer = cPtr;
        construct();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BulletBase) && (((BulletBase)obj).cPointer == this.cPointer);
    }

    @Override
    public int hashCode() {
        return (int)cPointer;
    }

    /**
     * @return The memory location (pointer) of this instance.
     */
    public long getCPointer() {
        return cPointer;
    }

    /**
     * Take ownership of the native instance, causing the native object to be deleted when this object gets out of scope.
     */
    public void takeOwnership() {
        cMemOwn = true;
    }

    /**
     * Release ownership of the native instance, causing the native object NOT to be deleted when this object gets out of
     * scope.
     */
    public void releaseOwnership() {
        cMemOwn = false;
    }

    /**
     * @return True if the native is destroyed when this object gets out of scope, false otherwise.
     */
    public boolean hasOwnership() {
        return cMemOwn;
    }

    /**
     * Deletes the bullet object this class encapsulates. Do not call directly, instead use the {@link #dispose()} method.
     */
    protected void deleteNative() {

    }

    @Override
    public void dispose() {
        if(refCount > 0 && Bullet.useRefCounting && Bullet.enableLogging)
            Gdx.app.error("Bullet", "Disposing " + toString() + " while it still has " + refCount + " references.");
        if(cMemOwn) {
            // Don't try to delete if this object did not create the pointer
            disposed = true;
            deleteNative();
            cPointer = 0;
        }
    }

    /**
     * @return Whether the {@link #dispose()} method of this instance is called.
     */
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public String toString() {
        return className + "(" + cPointer + "," + cMemOwn + ")";
    }

    protected void destroy() {
        try {
            if(destroyed && Bullet.enableLogging) Gdx.app.error("Bullet", "Already destroyed " + toString());
            destroyed = true;

            if(cMemOwn && !disposed) {
                if(Bullet.enableLogging)
                    Gdx.app.error("Bullet", "Disposing " + toString() + " due to garbage collection.");
                dispose();
            }
        }
        catch(Throwable e) {
            Gdx.app.error("Bullet", "Exception while destroying " + toString(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if(!destroyed && Bullet.enableLogging)
            Gdx.app.error("Bullet", "The " + className + " class does not override the finalize method.");
        super.finalize();
    }
}