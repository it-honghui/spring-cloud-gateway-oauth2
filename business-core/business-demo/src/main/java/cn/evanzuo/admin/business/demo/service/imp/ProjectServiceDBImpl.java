package cn.evanzuo.admin.business.demo.service.imp;

import cn.evanzuo.admin.business.demo.domain.Project;
import cn.evanzuo.admin.business.demo.mapper.ProjectMapper;
import cn.evanzuo.admin.business.demo.service.IProjectService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linlaoshi
 * @since 2022-12-31
 */
@Service
public class ProjectServiceDBImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
}
