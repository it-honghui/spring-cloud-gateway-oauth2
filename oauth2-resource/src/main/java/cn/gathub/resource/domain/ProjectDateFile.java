package cn.gathub.resource.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class ProjectDateFile {
  private Long id;
  private String fileType;
  private Integer length;
  private Integer number;
}
