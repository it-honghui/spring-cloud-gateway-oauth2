package cn.gathub.auth.service.impl;

import cn.gathub.auth.domain.entity.User;
import cn.gathub.auth.mapper.UserMapper;
import cn.gathub.auth.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Select;
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
