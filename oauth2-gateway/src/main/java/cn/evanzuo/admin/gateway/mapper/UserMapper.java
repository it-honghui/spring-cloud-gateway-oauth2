package cn.evanzuo.admin.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<Object> {
    @Select("select tb_wang_role.role_name from tb_wang_role" +
            "            left join tb_wang_url_role_relation t3 on tb_wang_role.id = t3.role_id" +
            "            left join tb_wang_auth_url url on t3.url_id = url.id" +
            "            where url.url = #{url};"
    )
    List<String> getData(@Param("url") String url);
}