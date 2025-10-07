package hanieum.conik.domain.project.entity;

import hanieum.conik.domain.project.enumerate.DeliveryStatus;
import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProgress extends AbstractEntity {
    private Long projectId;

    private ProjectProgressStep progressStep; // TODO : 각 스템 별로 전/중/후 ProgressStatus ENUM으로 관리하는 거 고민해보기

    private DeliveryStatus deliveryStatus;

    // TODO : 생성 시 프로젝트와의 데이터 일관성 유지하도록 수정
    public static ProjectProgress create(Long projectId, ProjectProgressStep step) {
        ProjectProgress progress = new ProjectProgress();
        progress.projectId = projectId;
        progress.progressStep = step;
        return progress;
    }
}
