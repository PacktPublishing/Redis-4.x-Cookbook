package packt.rediscookbook.springdataredisdemo.services;

import packt.rediscookbook.springdataredisdemo.model.User;

public interface UserService {
    User save(User user);
    User findById(String id);
    User update(User user);
    void delete(String id);
}
