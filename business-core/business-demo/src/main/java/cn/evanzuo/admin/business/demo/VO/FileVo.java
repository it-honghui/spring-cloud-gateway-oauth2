package cn.evanzuo.admin.business.demo.VO;

import cn.evanzuo.admin.business.demo.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class FileVo {
    private String date;
    private List<ProjectDateFile> list;
}
