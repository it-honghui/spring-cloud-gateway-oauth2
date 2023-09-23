package cn.gathub.business.demo.service.imp;

import cn.gathub.business.demo.domain.ProjectDate;
import cn.gathub.business.demo.mapper.ProjectDateMapper;
import cn.gathub.business.demo.service.IProjectDateService;
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
