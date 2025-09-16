package hanieum.conik.domain.company;

import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "company_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyDetail extends AbstractEntity {

    @Column(nullable = false, length = 2048)
    private String logoUrl; // 회사 로고 URL
}

