package cn.gathub.resourceMenu.service.imp;

import cn.gathub.resourceMenu.domain.Project;

import cn.gathub.resourceMenu.mapper.ProjectMapper;
import cn.gathub.resourceMenu.service.IProjectService;
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
