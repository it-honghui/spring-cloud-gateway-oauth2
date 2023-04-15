package cn.gathub.auth.mapper;

import cn.gathub.auth.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from tb_wang_user")
    List<Object> getData();
}