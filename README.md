# JMAP JavaMail Provider

This project provides a **Jakarta Mail (JavaMail) provider** backed by the JMAP
protocol. It registers custom `jmap` store and transport implementations so you
can use standard Jakarta Mail APIs to read and send mail against a JMAP server.

## Usage

### 1) Add the dependency

```gradle
dependencies {
    implementation 'de.hkuhn:jmap-javamail-provider:0.1.0'
}
```

### 2) Configure a Jakarta Mail session

```java
Properties props = new Properties();
props.put("mail.store.protocol", "jmap");
props.put("mail.transport.protocol", "jmap");

// JMAP server details (example keys; adjust to your implementation)
props.put("mail.jmap.apiUrl", "https://jmap.example.com/jmap");
props.put("mail.jmap.username", "user@example.com");
props.put("mail.jmap.password", "app-specific-password");

Session session = Session.getInstance(props);
```

### 3) Read mail using the JMAP store

```java
Store store = session.getStore("jmap");
store.connect();
Folder inbox = store.getFolder("INBOX");
inbox.open(Folder.READ_ONLY);
Message[] messages = inbox.getMessages();
```

### 4) Send mail using the JMAP transport

```java
Transport transport = session.getTransport("jmap");
transport.connect();
transport.sendMessage(message, message.getAllRecipients());
```

## Provider registration

The provider registers the following JavaMail entries:

- `protocol=jmap; type=store; class=jmap.read.JmapStore`
- `protocol=jmap; type=transport; class=jmap.send.JmapTransport`

When the JAR is on the classpath, Jakarta Mail discovers these entries via
`META-INF/javamail.providers`.
