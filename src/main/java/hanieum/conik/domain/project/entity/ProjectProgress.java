package hanieum.conik.domain.project.entity;

import hanieum.conik.domain.project.enumerate.DeliveryStatus;
import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProgress extends AbstractEntity {
    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private Long companyId; // TODO: ProjectProgress, ProjectProgressPicture / ProjectRequest 새로운 도메인으로 관리해야하는지 논의

    @Column(nullable = false)
    private ProjectProgressStep progressStep; // TODO : 각 스템 별로 전/중/후 ProgressStatus ENUM으로 관리하는 거 고민해보기

    private DeliveryStatus deliveryStatus;

    public static ProjectProgress create(Long projectId, Long companyId, ProjectProgressStep step) {
        ProjectProgress progress = new ProjectProgress();
        progress.projectId = projectId;
        progress.companyId = companyId;
        progress.progressStep = step;
        return progress;
    }
}
