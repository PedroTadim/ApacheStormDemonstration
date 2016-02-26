package NIO;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface AcceptHandler {
    void handleAccept(SelectionKey key) throws IOException;   
}
