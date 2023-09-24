package cn.evanzuo.admin.business.resource.service.imp;

import cn.evanzuo.admin.business.resource.domain.ProjectDate;
import cn.evanzuo.admin.business.resource.mapper.ProjectDateMapper;
import cn.evanzuo.admin.business.resource.service.IProjectDateService;
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
