package com.henry.service;

import com.henry.dao.UserMapper;
import com.henry.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUser(String username, String password) {
        return userMapper.selectByUsernameAndPassword(username, password);
    }

    public int modifyUser(User user) {
        return userMapper.updateUser(user);
    }

    public String addNewUser(User user) {
        // 1.依據username取得user
        User existUsernameUser = userMapper.selectByUsername(user.getUsername());
        User existEmailUser = userMapper.selectByEmail(user.getEmail());

        if (existUsernameUser != null) {
            // 2.如果user!=null，代表相同username的會員存在，返回訊息
            return " 相同名稱的用戶已存在，請改為不同名稱";
        } else if (existEmailUser != null) {
            return " 相同信箱的用戶已存在，請改為不同名稱";
        } else {
            // 3.如果user==null，新建user
            int num = userMapper.insert(user);

            if (num == 1) {
                // 4.返回新建使用者成功的訊息
                return null;
            } else {
                return "新增用戶id=" + user.getId() + "失敗";
            }
        }
    }

    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }

    public int modifyUserInfo(Integer userId, User user) {
        User orgUser = userMapper.selectById(userId);
        orgUser.setEmail(user.getEmail());
        orgUser.setName(user.getName());
        orgUser.setPhone(user.getPhone());
        int num = userMapper.updateUser(orgUser);
        return num;
    }
}
