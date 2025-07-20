package hanieum.conik.project.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectType {
    PRODUCT_DEVELOPMENT("제품 개발"),
    FINAL_PRODUCT("최종 제품 개발");

    private final String description;
}
