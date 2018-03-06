package packt.rediscookbook.mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import packt.rediscookbook.mapreduce.input.RedisHashInputFormat;
import packt.rediscookbook.mapreduce.output.RedisHashOutputFormat;
import packt.rediscookbook.mapreduce.output.RedisOutputMapper;

public class Application extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        int res = ToolRunner.run(conf, new Application(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        if (args.length != 6) {
            System.err
                    .println("Usage: AddBalance <redis hosts> <hash prefix> <balance> <partition length> <begin> <end>");
            System.exit(1);
        }

        String host = args[0];
        String hashPrefix = args[1];
        String balance = args[2];
        String pLength = args[3];
        String begin = args[4];
        String end = args[5];

        Job job = Job.getInstance(conf, "Add Balance");
        job.setNumReduceTasks(0);

        job.setJarByClass(Application.class);
        job.setMapperClass(RedisOutputMapper.class);

        RedisOutputMapper.setBalance(job,balance);

        job.setInputFormatClass(RedisHashInputFormat.class);
        RedisHashInputFormat.setRedisHost(job, host);
        RedisHashInputFormat.setHashPrefix(job, hashPrefix);
        RedisHashInputFormat.setBegin(job, begin);
        RedisHashInputFormat.setEnd(job, end);
        RedisHashInputFormat.setPLength(job, pLength);

        job.setOutputFormatClass(RedisHashOutputFormat.class);
        RedisHashOutputFormat.setRedisHost(job, host);
        RedisHashOutputFormat.setPLength(job, pLength);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //Wait for job completion
        return (job.waitForCompletion(true) ? 0 : 1);
    }
}
