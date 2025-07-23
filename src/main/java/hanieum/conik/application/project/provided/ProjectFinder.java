package hanieum.conik.application.project.provided;

import hanieum.conik.domain.project.entity.Project;

public interface ProjectFinder {
    Project findProject(Long projectId);
}
