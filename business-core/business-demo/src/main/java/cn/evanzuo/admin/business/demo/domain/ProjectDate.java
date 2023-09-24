package cn.evanzuo.admin.business.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectDate {
  private Long id;
  private String date;
  private Long projectId;
}
