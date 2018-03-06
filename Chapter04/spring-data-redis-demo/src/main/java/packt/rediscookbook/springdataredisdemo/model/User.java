package packt.rediscookbook.springdataredisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private String id;
    private String name;
    private String sex;
    private String nation;
    private long register_time;
}
