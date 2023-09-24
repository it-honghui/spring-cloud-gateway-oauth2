package cn.evanzuo.admin.business.demo.mapper;

import cn.evanzuo.admin.business.demo.domain.ProjectDateFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ProjectDateFileMapper extends BaseMapper<ProjectDateFile> {
    @Select("select *, file_type, `number`, length, `date` from tb_wang_project " +
            "left join tb_wang_project_date twpd on tb_wang_project.id = twpd.project_id " +
            "left join tb_wang_project_date_file twpf on twpd.id = twpf.project_date_id " +
            "where tb_wang_project.user_id = #{userId} and twpd.project_id = #{projectId}"
    )
    List<ProjectDateFile> getData(@Param("userId") String userId, @Param("projectId") String projectId);
}