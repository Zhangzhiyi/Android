package net.blogjava.mobile.aidl;
interface IMyService
{
    String getValue();
    
    void changeValue(in int value);
}