package cn.gathub.business.demo.DTO;

import cn.gathub.business.demo.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class ProjectListSave {
//    private String userId;
    private String date;
    private List<ProjectDateFile> list;
    private Long projectId;
}
