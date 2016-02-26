package NIO;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface RWHandler {

    void handleRead(SelectionKey key) throws IOException;

    void handleWrite(SelectionKey key) throws IOException;
}
