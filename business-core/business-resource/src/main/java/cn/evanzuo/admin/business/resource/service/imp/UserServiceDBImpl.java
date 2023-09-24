package cn.evanzuo.admin.business.resource.service.imp;

import cn.evanzuo.admin.business.resource.domain.User;
import cn.evanzuo.admin.business.resource.mapper.UserMapper;
import cn.evanzuo.admin.business.resource.service.IUserService;
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
public class UserServiceDBImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
