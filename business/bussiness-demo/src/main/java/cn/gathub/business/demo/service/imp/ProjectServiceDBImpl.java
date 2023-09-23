package cn.gathub.business.demo.service.imp;

import cn.gathub.business.demo.domain.Project;
import cn.gathub.business.demo.mapper.ProjectMapper;

import cn.gathub.business.demo.service.IProjectService;
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
