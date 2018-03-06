package packt.rediscookbook.mapreduce.input;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class RedisHashInputFormat extends InputFormat<Text, Text> {
    private static final int IDLENGTH = 10;
    public static final String REDIS_HOST_CONF = "mr.redishashinputformat.host";
    public static final String REDIS_HASH_PREFIX_CONF = "mr.redishashinputformat.hashprefix";
    public static final String REDIS_BEGIN_CONF = "mr.redishashinputformat.begin";
    public static final String REDIS_END_CONF = "mr.redishashinputformat.end";
    public static final String REDIS_PLENGTH_CONF = "mr.redishashinputformat.plength";

    public List<InputSplit> getSplits(JobContext jobContext) throws IOException, InterruptedException {
        String host = jobContext.getConfiguration().get(REDIS_HOST_CONF);
        String hashPrefix = jobContext.getConfiguration().get(REDIS_HASH_PREFIX_CONF);
        int begin = Integer.parseInt(jobContext.getConfiguration().get(REDIS_BEGIN_CONF));
        int end = Integer.parseInt(jobContext.getConfiguration().get(REDIS_END_CONF));
        int pLength = Integer.parseInt(jobContext.getConfiguration().get(REDIS_PLENGTH_CONF));

        // Create an input split for each host
        List<InputSplit> splits = new ArrayList<InputSplit>();
        String initKey="";
        for (int i = begin; i <= end; i++){
            String number = StringUtils.leftPad(String.valueOf(i), IDLENGTH, '0');
            String key = number.substring(0,IDLENGTH-pLength);
            if(!initKey.equals(key)){
                splits.add(new RedisHashInputSplit(host, hashPrefix,key));
                initKey = key;
            }
        }

        log.info("Input splits to process: " + splits.size());
        return splits;
    }

    public RecordReader<Text, Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new RedisHashRecordReader();
    }

    public static void setRedisHost(Job job, String host) {
        job.getConfiguration().set(REDIS_HOST_CONF, host);
    }
    public static void setHashPrefix(Job job, String prefix) {
        job.getConfiguration().set(REDIS_HASH_PREFIX_CONF, prefix);
    }

    public static void setBegin(Job job, String begin) {
        job.getConfiguration().set(REDIS_BEGIN_CONF, begin);
    }
    public static void setEnd(Job job, String end) {
        job.getConfiguration().set(REDIS_END_CONF, end);
    }
    public static void setPLength(Job job, String pLength) {
        job.getConfiguration().set(REDIS_PLENGTH_CONF, pLength);
    }
}
