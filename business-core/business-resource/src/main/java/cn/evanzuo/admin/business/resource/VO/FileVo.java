package cn.evanzuo.admin.business.resource.VO;

import cn.evanzuo.admin.business.resource.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class FileVo {
    private String date;
    private List<ProjectDateFile> list;
}
