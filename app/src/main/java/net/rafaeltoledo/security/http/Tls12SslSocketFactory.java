package net.rafaeltoledo.security.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Tls12SslSocketFactory extends SSLSocketFactory {
    private final String tag = "TLSv1.2";

    private SSLSocketFactory delegate;

    public Tls12SslSocketFactory() {
        delegate = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        SSLSocket s = (SSLSocket) delegate.createSocket(socket, host, port, autoClose);
        s.setEnabledProtocols(new String[]{tag});
        return s;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        SSLSocket s = (SSLSocket) delegate.createSocket(host, port);
        s.setEnabledProtocols(new String[]{tag});
        return s;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocket s = (SSLSocket) delegate.createSocket(host, port, localHost, localPort);
        s.setEnabledProtocols(new String[]{tag});
        return s;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket s = (SSLSocket) delegate.createSocket(host, port);
        s.setEnabledProtocols(new String[]{tag});
        return s;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocket s = (SSLSocket) delegate.createSocket(address, port, localAddress, localPort);
        s.setEnabledProtocols(new String[]{tag});
        return s;
    }
}
