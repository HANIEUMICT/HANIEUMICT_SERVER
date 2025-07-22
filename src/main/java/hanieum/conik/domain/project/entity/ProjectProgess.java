package hanieum.conik.domain.project.entity;

import hanieum.conik.domain.proposal.domain.entity.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectProgess extends AbstractEntity {

}
