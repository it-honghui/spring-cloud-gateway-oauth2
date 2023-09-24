package cn.evanzuo.admin.auth.service.impl;

import cn.evanzuo.admin.auth.mapper.UserMapper;
import cn.evanzuo.admin.auth.service.IUserService;
import cn.evanzuo.admin.auth.domain.entity.User;
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
