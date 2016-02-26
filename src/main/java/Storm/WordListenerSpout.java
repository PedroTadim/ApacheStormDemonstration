package Storm;

import NIO.NIOServer;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import java.io.IOException;
import java.util.Map;

public class WordListenerSpout extends BaseRichSpout {

    private NIOServer _NIOServer;

    @Override
    public void open(Map conf, TopologyContext cont, SpoutOutputCollector collect) {
        try {
            _NIOServer = new NIOServer(collect);
        } catch (IOException ex) {
            this.close();
        }
    }

    @Override
    public void close() {
        _NIOServer.closeServer();
    }

    @Override
    public void nextTuple() {
        _NIOServer.serverCycle();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("words"));
    }
}
