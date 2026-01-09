package hanieum.conik.domain.project;

import hanieum.conik.domain.project.entity.Project;
import hanieum.conik.domain.project.enumerate.ProjectStatus;

import java.time.LocalDate;

public final class ProjectFixtures {
    private ProjectFixtures() {}

    public static Project minimal(Long memberId, String title) {
        return Project.create(
                memberId,                 // memberId (NOT NULL 가능성 높음)
                title,                    // projectTitle
                "CATEGORY",               // category
                "DETAIL",                 // categoryDetail
                null,                     // categoryDetailEtc
                "PURPOSE",                // purpose
                null,                     // purposeEtc
                100,                      // projectQuantity
                "REQUESTS",               // requests
                LocalDate.now().plusDays(7),     // deadline
                false,                    // canDeadlineChange
                10_000,                   // requestEstimate
                LocalDate.now().plusDays(30),    // publicUntil
                ProjectStatus.PUBLIC,     // projectStatus
                true,                     // canPhoneConsult
                "Seoul, Korea"            // deliveryAddress
        );
    }

    public static Project minimal(String title) {
        return minimal(999L, title); // 테스트용 기본 memberId
    }
}
