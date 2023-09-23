package cn.gathub.business.demo.service.imp;

import cn.gathub.business.demo.domain.User;
import cn.gathub.business.demo.mapper.UserMapper;
import cn.gathub.business.demo.service.IUserService;
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
