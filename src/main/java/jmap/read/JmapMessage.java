package jmap.read;

import jakarta.mail.Folder;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

public class JmapMessage extends MimeMessage {
    private final Folder folder;

    public JmapMessage(Folder folder) {
        super(folder.getStore().getSession());
        this.folder = folder;
    }

    public JmapMessage(Session session) {
        super(session);
        this.folder = null;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    void setJmapMessageNumber(int number) throws MessagingException {
        setMessageNumber(number);
    }
}
