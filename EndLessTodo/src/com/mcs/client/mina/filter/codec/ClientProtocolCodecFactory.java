package com.mcs.client.mina.filter.codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ClientProtocolCodecFactory implements ProtocolCodecFactory {

    /**
     * The ProtocolEncoder
     */
    private final ProtocolEncoder encoder;

    /**
     * The ProtocolDecoder
     */
    private final ProtocolDecoder decoder;

    /**
     * Constructor
     */
    public ClientProtocolCodecFactory() {

        encoder = new ClientProtocolEncoder();
        decoder = new ClientProtocolDecoder();
    }

    /**
     * Returns the ProtocolEncoder
     * @return the ProtocolEncoder
     */
    @Override
    public ProtocolEncoder getEncoder() {
        return encoder;
    }

    /**
     * Returns the ProtocolDecoder
     * @return the ProtocolDecoder
     */
    @Override
    public ProtocolDecoder getDecoder() {
        return decoder;
    }
}
