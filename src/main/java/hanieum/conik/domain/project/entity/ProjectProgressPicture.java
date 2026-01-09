package hanieum.conik.domain.project.entity;

import hanieum.conik.domain.project.enumerate.ProjectProgressStep;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProgressPicture extends AbstractEntity {
    private Long projectProgressId;

    private ProjectProgressStep projectProgressStep;

    private String pictureUrl;
}
