package packt.rediscookbook.mapreduce.output;

import lombok.extern.log4j.Log4j;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class RedisHashRecordWriter extends RecordWriter<Text, Text> {
    private static final int IDLENGTH = 10;
    private final int pLength;
    private final String prefix;
    private Jedis jedis;

    public RedisHashRecordWriter(
            String host, String pLength, String prefix) {
        this.pLength = Integer.parseInt(pLength);
        this.prefix = prefix;
        jedis = new Jedis(host);
        jedis.connect();
    }

    public void write(Text key, Text value)
            throws IOException, InterruptedException {
        String key1 = key.toString().substring(0,IDLENGTH-pLength);
        String key2 = key.toString().substring(IDLENGTH-pLength);
        jedis.hset(prefix+":"+key1,key2,value.toString());
    }

    public void close(TaskAttemptContext taskAttemptContext)
            throws IOException, InterruptedException {
        jedis.close();
    }
}
