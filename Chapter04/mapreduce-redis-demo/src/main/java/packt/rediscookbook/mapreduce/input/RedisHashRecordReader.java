package packt.rediscookbook.mapreduce.input;

import lombok.extern.log4j.Log4j;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Log4j
public class RedisHashRecordReader extends RecordReader<Text, Text> {
    private Iterator<Map.Entry<String, String>> keyValueMapIter = null;
    private Text rrKey = new Text(), rrValue = new Text();
    private float processedKVs = 0, totalKVs = 0;
    private Map.Entry<String, String> currentEntry = null;
    private String prefix,host,key;
    private Jedis jedis;

    public void initialize(InputSplit split, TaskAttemptContext taskAttemptContext)
            throws IOException, InterruptedException {
        host = split.getLocations()[0];
        prefix = ((RedisHashInputSplit) split).getPrefix();
        key = ((RedisHashInputSplit) split).getKey();
        String hashKey = prefix+":"+key;

        jedis = new Jedis(host);
        log.info("Connect to " + host);
        jedis.connect();
        jedis.getClient().setTimeoutInfinite();

        totalKVs = jedis.hlen(hashKey);
        keyValueMapIter = jedis.hgetAll(hashKey).entrySet().iterator();
    }

    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (keyValueMapIter.hasNext()) {
            currentEntry = keyValueMapIter.next();
            rrKey.set(key+currentEntry.getKey());
            rrValue.set(currentEntry.getValue());
            return true;
        } else {
            return false;
        }
    }

    public Text getCurrentKey() throws IOException, InterruptedException {
        return rrKey;
    }

    public Text getCurrentValue() throws IOException, InterruptedException {
        return rrValue;
    }

    public float getProgress() throws IOException, InterruptedException {
        return processedKVs / totalKVs;
    }

    public void close() throws IOException {
        jedis.close();
    }
}
