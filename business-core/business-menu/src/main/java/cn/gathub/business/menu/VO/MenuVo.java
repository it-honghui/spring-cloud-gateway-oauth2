package cn.gathub.business.menu.VO;

import cn.gathub.business.menu.domain.ProjectMenu;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Integer total;
    private List<ProjectMenu> list;
}
