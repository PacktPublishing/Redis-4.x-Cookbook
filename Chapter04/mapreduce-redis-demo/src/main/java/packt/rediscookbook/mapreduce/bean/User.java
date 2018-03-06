package packt.rediscookbook.mapreduce.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String name;
    private String sex;
    private long rtime;
    private String nation;
    private long balance;
}
