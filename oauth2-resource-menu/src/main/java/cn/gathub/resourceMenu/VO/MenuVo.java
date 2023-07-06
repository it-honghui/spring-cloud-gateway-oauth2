package cn.gathub.resourceMenu.VO;

import cn.gathub.resourceMenu.domain.ProjectMenu;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Integer total;
    private List<ProjectMenu> list;
}
