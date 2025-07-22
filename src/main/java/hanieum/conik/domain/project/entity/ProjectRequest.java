package hanieum.conik.domain.project.entity;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRequest extends AbstractEntity {
    private Long projectId;

    private Long companyId;
}
