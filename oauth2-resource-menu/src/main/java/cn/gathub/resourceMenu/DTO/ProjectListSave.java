package cn.gathub.resourceMenu.DTO;

import cn.gathub.resourceMenu.domain.ProjectDateFile;
import lombok.Data;

import java.util.List;

@Data
public class ProjectListSave {
//    private String userId;
    private String date;
    private List<ProjectDateFile> list;
    private Long projectId;
}
