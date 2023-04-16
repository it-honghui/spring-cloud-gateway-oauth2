package cn.gathub.gateway.service.imp;

import cn.gathub.gateway.mapper.UserMapper;
import cn.gathub.gateway.service.IUserService;
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
public class UserServiceDBImpl extends ServiceImpl<UserMapper, Object> implements IUserService {
}
