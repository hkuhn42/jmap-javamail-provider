package jmap.send;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.URLName;
import okhttp3.HttpUrl;
import rs.ltt.jmap.client.JmapClient;

public class JmapTransport extends Transport {
    private static final String PROP_BASE_URL = "mail.jmap.baseUrl";
    private static final String PROP_USERNAME = "mail.jmap.username";
    private static final String PROP_PASSWORD = "mail.jmap.password";

    private JmapClient client;
    private String baseUrl;

    public JmapTransport(Session session, URLName urlname) {
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

    @Override
    public void sendMessage(Message message, jakarta.mail.Address[] addresses) throws MessagingException {
        if (!isConnected()) {
            throw new MessagingException("Transport is not connected");
        }
        throw new MessagingException("Sending messages is not implemented yet for JMAP transport");
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
