package com.wama.backend;
import java.io.IOException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;


public class SSLFactory extends LogClass {
    private SSLServerSocketFactory serverSocketFactory;
    private static final String KEYSTORE_FILE;
    private static final String KEYSTORE_PASSWORD;

    static {
        KEYSTORE_FILE = "keystore.jks";
        KEYSTORE_PASSWORD = "password";
    }

    private SSLFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            serverSocketFactory = sslContext.getServerSocketFactory();
        } catch (Exception e) {
            error("Error creating SSLFactory: " + e.getMessage(), e);
        }
    }

    private static final class InstanceHolder {
        // My IDE yelled at me for having double-checked locking and instead created this Holder for me

        // Can only be created once, by nature of Java class loading
        private static final SSLFactory instance = new SSLFactory();
    }

    public static SSLFactory getInstance() {
        // Singleton pattern using a Holder class instead of double-checked locking
        return InstanceHolder.instance;
    }

    public SSLServerSocket createSSLServerSocket(int port) throws IOException {
        return (SSLServerSocket) serverSocketFactory.createServerSocket(port);
    }
}