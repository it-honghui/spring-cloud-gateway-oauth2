package cn.evanzuo.admin.business.resource.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
public class Project {
  private Long id;
  private String projectName;
  private String userId;
}
