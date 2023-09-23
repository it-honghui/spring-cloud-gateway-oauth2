package cn.gathub.business.resource.service.imp;

import cn.gathub.business.resource.domain.Project;

import cn.gathub.business.resource.mapper.ProjectMapper;
import cn.gathub.business.resource.service.IProjectService;
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
