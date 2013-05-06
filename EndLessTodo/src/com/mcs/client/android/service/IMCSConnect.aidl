package com.mcs.client.android.service;

interface IMCSConnect {
    void connectToServier(in String id, in byte[] cred);
    void disconnect();
}
