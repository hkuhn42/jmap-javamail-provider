package jmap.read;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.URLName;
import okhttp3.HttpUrl;
import rs.ltt.jmap.client.JmapClient;

public class JmapStore extends Store {
    private static final String PROP_BASE_URL = "mail.jmap.baseUrl";
    private static final String PROP_USERNAME = "mail.jmap.username";
    private static final String PROP_PASSWORD = "mail.jmap.password";

    private JmapClient client;
    private String baseUrl;

    public JmapStore(Session session, URLName urlname) {
        super(session, urlname);
    }

    @Override
    protected boolean protocolConnect(String host, int port, String user, String password) throws MessagingException {
        String resolvedUser = user != null ? user : session.getProperty(PROP_USERNAME);
        String resolvedPassword = password != null ? password : session.getProperty(PROP_PASSWORD);
        baseUrl = resolveBaseUrl(host, port);
        if (resolvedUser == null || resolvedPassword == null || baseUrl == null) {
            return false;
        }
        HttpUrl httpUrl = HttpUrl.get(baseUrl);
        client = new JmapClient(resolvedUser, resolvedPassword, httpUrl);
        return true;
    }

    public JmapClient getClient() {
        return client;
    }

    @Override
    public Folder getDefaultFolder() throws MessagingException {
        checkConnected();
        return new JmapFolder(this, "");
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        checkConnected();
        return new JmapFolder(this, name);
    }

    @Override
    public Folder getFolder(URLName url) throws MessagingException {
        checkConnected();
        return new JmapFolder(this, url.getFile());
    }

    @Override
    public synchronized void close() throws MessagingException {
        try {
            if (client != null) {
                client.close();
            }
        } finally {
            client = null;
            super.close();
        }
    }

    private void checkConnected() throws MessagingException {
        if (!isConnected()) {
            throw new MessagingException("Store is not connected");
        }
    }

    private String resolveBaseUrl(String host, int port) {
        if (baseUrl != null) {
            return baseUrl;
        }
        String configured = session.getProperty(PROP_BASE_URL);
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        if (host == null) {
            return null;
        }
        String scheme = "https";
        if (port > 0) {
            return String.format("%s://%s:%d", scheme, host, port);
        }
        return String.format("%s://%s", scheme, host);
    }
}
