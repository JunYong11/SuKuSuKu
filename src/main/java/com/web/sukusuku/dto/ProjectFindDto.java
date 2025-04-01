package com.web.sukusuku.dto;

import com.web.sukusuku.model.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFindDto {
	
	private Long projectId;
    private String projectName;

    public ProjectFindDto(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
    }

}
