package Storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import java.util.Map;

public class PrinterBolt extends BaseRichBolt {

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {

    }

    @Override
    public void prepare(Map map, TopologyContext tc, OutputCollector oc) {

    }

    @Override
    public void execute(Tuple tuple) {
        int count = tuple.getIntegerByField("count");
        System.out.println("Received a line with " + count + " words.");
    }
}
