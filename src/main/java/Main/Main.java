package Main;

import Storm.PrinterBolt;
import Storm.WordCounterBolt;
import Storm.WordListenerSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class Main {

    private static final LocalCluster Cluster = new LocalCluster();

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("socket", new WordListenerSpout(), 1);
        builder.setBolt("counter", new WordCounterBolt(), 5)
                .shuffleGrouping("socket");
        builder.setBolt("printer", new PrinterBolt(), 5)
                .shuffleGrouping("counter");

        Config conf = new Config();
        conf.setNumWorkers(1);
        Cluster.submitTopology("wordcounter", conf, builder.createTopology());
        while (true) {
            Utils.sleep(10000);
        }
    }
}
