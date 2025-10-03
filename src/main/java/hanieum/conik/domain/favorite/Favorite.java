package hanieum.conik.domain.favorite;

import hanieum.conik.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "favorite",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_favorite_company_project",
                columnNames = {"company_id", "project_id"}
        )
)
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long projectId;

    private Favorite(Long companyId, Long projectId) {
        this.companyId = companyId;
        this.projectId = projectId;
    }

    public static Favorite create(Long companyId, Long projectId) {
        return new Favorite(companyId, projectId);
    }
}
