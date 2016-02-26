package NIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class NIOBaseLineHandler implements RWHandler {

    private static final int NIO_BUFFERS_SIZE = 128;

    private final ByteBuffer LineRead = ByteBuffer.allocate(NIO_BUFFERS_SIZE);

    private final ByteBuffer InputBuffer = ByteBuffer.allocate(NIO_BUFFERS_SIZE);

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel socket = (SocketChannel) key.channel();
        socket.read(InputBuffer);
        InputBuffer.flip();
        if (!InputBuffer.hasRemaining()) {
            socket.close();
            return;
        }

        while (InputBuffer.hasRemaining()) {
            byte b = InputBuffer.get();
            while (InputBuffer.hasRemaining() && b != '\n') {
                LineRead.put(b);
                b = InputBuffer.get();
            }
            if (b == '\n') {
                LineRead.flip();
                handleReadLine(key, LineRead);
                LineRead.clear();
            }
        }
        InputBuffer.clear();
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected abstract void handleReadLine(SelectionKey key, ByteBuffer buf) throws IOException;
}
