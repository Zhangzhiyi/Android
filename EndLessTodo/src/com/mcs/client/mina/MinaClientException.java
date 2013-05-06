package com.mcs.client.mina;

public class MinaClientException extends RuntimeException {

	private static final long serialVersionUID = -3502262948135112593L;

    public MinaClientException() {
        super();
    }

    public MinaClientException(String message) {
        super(message);
    }

    public MinaClientException(Throwable cause) {
        super(cause);
    }

    public MinaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
