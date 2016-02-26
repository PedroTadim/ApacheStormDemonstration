package NIO;

import backtype.storm.spout.SpoutOutputCollector;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer implements AcceptHandler {

    public static final int PORT = 9999;

    private final ServerSocketChannel Server;

    private final Selector NIOSelector;
    
    private final SpoutOutputCollector Colector;

    public NIOServer(SpoutOutputCollector colector) throws IOException {
        Server = ServerSocketChannel.open();
        Server.bind(new InetSocketAddress(PORT));
        NIOSelector = Selector.open();
        Server.configureBlocking(false);
        this.Colector = colector;
    }

    public void serverCycle() {
        try {
            Server.register(NIOSelector, SelectionKey.OP_ACCEPT, this);
            while (true) {
                NIOSelector.select();
                for (Iterator<SelectionKey> i = NIOSelector.selectedKeys().iterator(); i.hasNext();) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {
                        AcceptHandler h = (AcceptHandler) key.attachment();
                        h.handleAccept(key);
                    } else if (key.isReadable()) {
                        RWHandler h = (RWHandler) key.attachment();
                        h.handleRead(key);
                    } else if (key.isWritable()) {
                        RWHandler h = (RWHandler) key.attachment();
                        h.handleWrite(key);
                    }

                    i.remove();
                }
            }
        } catch (IOException ex) {
            this.closeServer();
        }

    }

    public void closeServer() {
        try {
            NIOSelector.close();
            Server.socket().close();
            Server.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel c = (ServerSocketChannel) key.channel();
        SocketChannel s = c.accept();
        s.configureBlocking(false);
        s.register(key.selector(), SelectionKey.OP_READ, new StormLineReader(Colector));
    }
}
