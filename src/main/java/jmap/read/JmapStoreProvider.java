package jmap.read;

import jakarta.mail.Provider;

public class JmapStoreProvider extends Provider {
    public JmapStoreProvider() {
        super(Type.STORE, "jmap", JmapStore.class.getName(), "jmap", "0.1.0");
    }
}
