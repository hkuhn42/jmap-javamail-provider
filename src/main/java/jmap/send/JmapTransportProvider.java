package jmap.send;

import jakarta.mail.Provider;

public class JmapTransportProvider extends Provider {
    public JmapTransportProvider() {
        super(Type.TRANSPORT, "jmap", JmapTransport.class.getName(), "jmap", "0.1.0");
    }
}
