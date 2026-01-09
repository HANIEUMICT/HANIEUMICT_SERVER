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

    private ProjectProgressStep progressStep;

    private DeliveryStatus deliveryStatus;
}
