package Storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;

public class WordCounterBolt extends BaseRichBolt {

    private OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext cont, OutputCollector collect) {
        _collector = collect;
    }

    @Override
    public void execute(Tuple tuple) {
        String words = tuple.getStringByField("words").trim();
        int count = words.isEmpty() ? 0 : words.split("\\s+").length;
        _collector.emit(tuple, new Values(count));
        _collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("count"));
    }
}
