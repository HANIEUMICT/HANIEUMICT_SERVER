package hanieum.conik.domain.project.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectType {
    PRODUCT_DEVELOPMENT("제품 개발 참여"),
    FINAL_PRODUCT("완성 제품 제조");

    private final String description;
}
