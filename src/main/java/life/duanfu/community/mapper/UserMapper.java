package life.duanfu.community.mapper;

import life.duanfu.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values " +
            "(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

    //#{token}，在mybatis语法中它会把形参String token 的值放进去
    //是类的时候会自动放进去，不是类的时候需要加一个注解@Param("token")
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token")String token);
}
