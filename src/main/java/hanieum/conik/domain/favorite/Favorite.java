package hanieum.conik.domain.favorite;

import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "companyId", nullable = false)
    private Long companyId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    private Favorite(Long companyId, Long projectId) {
        this.companyId = companyId;
        this.projectId = projectId;
    }

    public static Favorite create(Long companyId, Long projectId) {
        return new Favorite(companyId, projectId);
    }
}
