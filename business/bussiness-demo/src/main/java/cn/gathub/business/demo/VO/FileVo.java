package cn.gathub.business.demo.VO;

import cn.gathub.business.demo.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class FileVo {
    private String date;
    private List<ProjectDateFile> list;
}
