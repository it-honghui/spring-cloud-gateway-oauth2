package cn.gathub.resource.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class Project {
  private Long id;
  private String projectName;
}
