package cn.evanzuo.admin.business.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
public class ProjectDateFile {
  private Long id;
  private String fileType;
  private Integer length;
  private Integer number;
  private String date;
  private Long projectDateId;
}
