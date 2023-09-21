package cn.gathub.resource.VO;

import cn.gathub.resource.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class FileVo {
    private String date;
    private List<ProjectDateFile> list;
}
