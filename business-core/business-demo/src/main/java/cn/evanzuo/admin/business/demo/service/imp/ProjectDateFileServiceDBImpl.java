package cn.evanzuo.admin.business.demo.service.imp;

import cn.evanzuo.admin.business.demo.domain.ProjectDateFile;
import cn.evanzuo.admin.business.demo.mapper.ProjectDateFileMapper;
import cn.evanzuo.admin.business.demo.service.IProjectDateFileService;
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
public class ProjectDateFileServiceDBImpl extends ServiceImpl<ProjectDateFileMapper, ProjectDateFile> implements IProjectDateFileService {
}
