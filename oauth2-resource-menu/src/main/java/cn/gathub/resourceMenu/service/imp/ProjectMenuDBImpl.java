package cn.gathub.resourceMenu.service.imp;

import cn.gathub.resourceMenu.domain.ProjectMenu;
import cn.gathub.resourceMenu.mapper.MenuMapper;
import cn.gathub.resourceMenu.service.IMenuSerive;
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
public class ProjectMenuDBImpl extends ServiceImpl<MenuMapper, ProjectMenu> implements IMenuSerive {
}
