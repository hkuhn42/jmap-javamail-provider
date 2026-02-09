package jmap.read;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;

public class JmapFolder extends Folder {
    private final String name;
    private boolean open;

    protected JmapFolder(Store store, String name) {
        super(store);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public Folder getParent() {
        return null;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public Folder[] list(String pattern) throws MessagingException {
        return new Folder[0];
    }

    @Override
    public char getSeparator() {
        return '/';
    }

    @Override
    public int getType() {
        return HOLDS_MESSAGES;
    }

    @Override
    public boolean create(int type) throws MessagingException {
        return false;
    }

    @Override
    public boolean hasNewMessages() throws MessagingException {
        return false;
    }

    @Override
    public Folder getFolder(String name) throws MessagingException {
        return new JmapFolder(getStore(), name);
    }

    @Override
    public boolean delete(boolean recurse) throws MessagingException {
        return false;
    }

    @Override
    public boolean renameTo(Folder f) throws MessagingException {
        return false;
    }

    @Override
    public void open(int mode) throws MessagingException {
        open = true;
    }

    @Override
    public void close(boolean expunge) throws MessagingException {
        open = false;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public Flags getPermanentFlags() {
        return new Flags();
    }

    @Override
    public int getMessageCount() throws MessagingException {
        return 0;
    }

    @Override
    public Message getMessage(int msgnum) throws MessagingException {
        JmapMessage message = new JmapMessage(this);
        message.setJmapMessageNumber(msgnum);
        return message;
    }

    @Override
    public void appendMessages(Message[] msgs) throws MessagingException {
        throw new MessagingException("Appending messages is not supported");
    }

    @Override
    public Message[] expunge() throws MessagingException {
        return new Message[0];
    }
}
