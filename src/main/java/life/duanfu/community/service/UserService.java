package life.duanfu.community.service;

import life.duanfu.community.mapper.UserMapper;
import life.duanfu.community.model.User;
import life.duanfu.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //对于当前我们的github用户，accountId是唯一的，所以我们需要在它使用github登陆成功的时候，
        //通过accountId去查一下数据库有没有这个数据，如果有的话，我们就把最新的token更新进去，
        //就相当于之前我的登录态是不需要的，我需要把token刷新一下。
        //所以我们需要实现的逻辑是，通过数据库能查到accountId等于当前我登录成功的accountId的话我去把当前的token更新掉
        //如果没有我做插入操作
        //User dbUser = userMapper.findByAccountId(user.getAccountId());
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);

        if (users.size() == 0) {
            //insert
            //因为update的时候是不需要创建时间的所以把时间拿进来
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //update
            //因为修改时间，头像，名字，token都会变化
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            //传进来的值不为空的时候再使用这个方法
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            //userMapper.update(dbUser);
            userMapper.updateByExampleSelective(updateUser, example);
        }

    }
}
