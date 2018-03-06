package packt.rediscookbook.springdataredisdemo.services;


import packt.rediscookbook.springdataredisdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERKEY = "user";

    private HashOperations operations;

    @Autowired
    private RedisTemplate<String,User> redisTemplate;

    @PostConstruct
    public void initOperations(){
        this.operations = redisTemplate.opsForHash();
    }

    @Override
    public User save(User user) {
        this.operations.put(USERKEY, user.getId(),user);
        return user;
    }

    @Override
    public User findById(String id) {
        return (User) this.operations.get(USERKEY,id);
    }

    @Override
    public User update(User user) {
        save(user);
        return user;
    }

    @Override
    public void delete(String id) {
        this.operations.delete(USERKEY,id);
    }
}
