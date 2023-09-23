package cn.gathub.resource.service.imp;

import cn.gathub.resource.domain.ProjectDate;
import cn.gathub.resource.mapper.ProjectDateMapper;
import cn.gathub.resource.service.IProjectDateService;
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
public class ProjectDateServiceDBImpl extends ServiceImpl<ProjectDateMapper, ProjectDate> implements IProjectDateService {
}
