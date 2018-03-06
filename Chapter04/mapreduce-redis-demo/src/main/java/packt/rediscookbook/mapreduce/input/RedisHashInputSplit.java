package packt.rediscookbook.mapreduce.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisHashInputSplit extends InputSplit implements Writable {
    private String host;
    private String prefix;
    private String key;

    public void write(DataOutput out) throws IOException {
        out.writeUTF(host);
        out.writeUTF(prefix);
        out.writeUTF(key);
    }

    public void readFields(DataInput in) throws IOException {
        this.host = in.readUTF();
        this.prefix = in.readUTF();
        this.key = in.readUTF();
    }

    public long getLength() throws IOException, InterruptedException {
        return 0;
    }

    public String[] getLocations() throws IOException, InterruptedException {
        return new String[] { host };
    }
}
