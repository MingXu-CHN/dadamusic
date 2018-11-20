/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/hanamingming/Downloads/android_day13_pm_v2_音乐播放器完结/哒哒音乐/src/cn/tedu/mediaplayer/aidl/MusicInterface.aidl
 */
package cn.tedu.mediaplayer.aidl;
public interface MusicInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.tedu.mediaplayer.aidl.MusicInterface
{
private static final java.lang.String DESCRIPTOR = "cn.tedu.mediaplayer.aidl.MusicInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.tedu.mediaplayer.aidl.MusicInterface interface,
 * generating a proxy if needed.
 */
public static cn.tedu.mediaplayer.aidl.MusicInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.tedu.mediaplayer.aidl.MusicInterface))) {
return ((cn.tedu.mediaplayer.aidl.MusicInterface)iin);
}
return new cn.tedu.mediaplayer.aidl.MusicInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_playMusic:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.playMusic(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_playOrPause:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.playOrPause();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getPlayState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlayState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_seekTo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.seekTo(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.tedu.mediaplayer.aidl.MusicInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void playMusic(java.lang.String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_playMusic, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int playOrPause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playOrPause, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getPlayState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void seekTo(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_seekTo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_playMusic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_playOrPause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPlayState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_seekTo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void playMusic(java.lang.String url) throws android.os.RemoteException;
public int playOrPause() throws android.os.RemoteException;
public int getPlayState() throws android.os.RemoteException;
public void seekTo(int position) throws android.os.RemoteException;
}
