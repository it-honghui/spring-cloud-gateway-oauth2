package cn.evanzuo.admin.business.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
public class Project {
  private Long id;
  private String projectName;
  private String userId;
}
