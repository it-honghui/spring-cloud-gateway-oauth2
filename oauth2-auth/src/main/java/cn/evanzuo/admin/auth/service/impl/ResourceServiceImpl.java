package cn.evanzuo.admin.auth.service.impl;

import cn.evanzuo.admin.auth.service.ResourceService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 资源与角色匹配关系管理业务类
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Service
public class ResourceServiceImpl implements ResourceService {

  private final RedisTemplate<String, Object> redisTemplate;

  public ResourceServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
}
