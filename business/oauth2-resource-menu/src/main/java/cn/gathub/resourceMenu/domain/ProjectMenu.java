package cn.gathub.resourceMenu.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
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
