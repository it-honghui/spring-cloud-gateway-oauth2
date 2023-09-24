package cn.evanzuo.admin.business.menu.VO;

import cn.evanzuo.admin.business.menu.domain.ProjectMenu;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Integer total;
    private List<ProjectMenu> list;
}
