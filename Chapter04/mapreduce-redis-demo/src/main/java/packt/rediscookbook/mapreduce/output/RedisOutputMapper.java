package packt.rediscookbook.mapreduce.output;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.codehaus.jackson.map.ObjectMapper;
import packt.rediscookbook.mapreduce.bean.User;

import java.io.IOException;

public class RedisOutputMapper extends Mapper<Object, Text, Text, Text> {
    public static final String REDIS_BALANCE_CONF = "mr.redishashinputformat.blance";
    private Text outkey = new Text();
    private Text outvalue = new Text();

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        long addBalance = Long.parseLong(context.getConfiguration().get(REDIS_BALANCE_CONF));
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(value.toString(), User.class);

        user.setBalance(addBalance+user.getBalance());
        // Set our output key and values
        outkey.set((Text)key);
        outvalue.set(mapper.writeValueAsString(user));

        context.write(outkey, outvalue);
    }

    public static void setBalance(Job job, String balance) {
        job.getConfiguration().set(REDIS_BALANCE_CONF,balance);
    }
}
