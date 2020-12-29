// IBinderPool.aidl
package com.aidlserver;

// Declare any non-default types here with import statements

interface IBinderPool {
     IBinder queryBinder(int type);
}
