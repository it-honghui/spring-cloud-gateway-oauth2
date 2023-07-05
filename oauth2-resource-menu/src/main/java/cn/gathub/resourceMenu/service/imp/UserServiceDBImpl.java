package cn.gathub.resourceMenu.service.imp;

import cn.gathub.resourceMenu.domain.User;
import cn.gathub.resourceMenu.mapper.UserMapper;
import cn.gathub.resourceMenu.service.IUserService;
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
