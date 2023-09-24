package cn.evanzuo.admin.business.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
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
