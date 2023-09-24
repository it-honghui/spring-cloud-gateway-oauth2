package cn.evanzuo.admin.auth.mapper;

import cn.evanzuo.admin.auth.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select tb_wang_role.role_name from tb_wang_role " +
            "left join tb_wang_user_role_relation t3 on tb_wang_role.id = t3.role_id " +
            "left join tb_wang_user user on t3.user_id = user.id " +
            "where user.username = #{username};"
    )
    List<String> getData(@Param("username") String username);


    @Select("select * from tb_wang_user " +
            "where username = #{username};"
    )
    User getUserInfo(@Param("username") String username);
}