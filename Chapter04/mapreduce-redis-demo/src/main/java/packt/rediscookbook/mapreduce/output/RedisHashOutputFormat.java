package packt.rediscookbook.mapreduce.output;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import java.io.IOException;

public class RedisHashOutputFormat extends OutputFormat<Text, Text> {
    public static final String REDIS_HOST_CONF = "mr.redishashinputformat.host";
    public static final String REDIS_PLENGTH_CONF = "mr.redishashinputformat.plegnth";
    public static final String REDIS_HASH_PREFIX_CONF = "mr.redishashinputformat.hashprefix";
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new RedisHashRecordWriter(
                context.getConfiguration().get(REDIS_HOST_CONF),
                context.getConfiguration().get(REDIS_PLENGTH_CONF),
                context.getConfiguration().get(REDIS_HASH_PREFIX_CONF)
        );
    }

    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException { }

    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        return (new NullOutputFormat<Text, Text>())
                .getOutputCommitter(context);
    }

    public static void setRedisHost(Job job, String host) {
        job.getConfiguration().set(REDIS_HOST_CONF,host);
    }

    public static void setPLength(Job job, String pLength) {
        job.getConfiguration().set(REDIS_PLENGTH_CONF,pLength);
    }
}
