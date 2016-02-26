package NIO;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.tuple.Values;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;

public class StormLineReader extends NIOBaseLineHandler {
    
    private final SpoutOutputCollector Colector;
    
    public StormLineReader(SpoutOutputCollector colector) {
        this.Colector = colector;
    }
    
    @Override
    protected void handleReadLine(SelectionKey key, ByteBuffer buf) {
        String words = new String(buf.array(), Charset.forName("UTF-8"));
        Colector.emit(new Values(words));
    }
    
}
