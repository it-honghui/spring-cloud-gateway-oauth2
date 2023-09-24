package cn.evanzuo.admin.business.menu.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
@TableName(value = "pms_category")
public class ProjectMenu {
  private Long catId;
  private String name;
  private Long parentCid;
  private Long catLevel;
  private Long showStatus;

  private int sort;

  @TableField(exist = false)
  private List<ProjectMenu> children;
}
