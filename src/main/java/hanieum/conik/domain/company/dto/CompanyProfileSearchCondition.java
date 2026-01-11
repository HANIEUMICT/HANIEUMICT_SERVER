package hanieum.conik.domain.company.dto;

import java.util.List;

/**
 * 검색/필터 조건 DTO
 */
public record CompanyProfileSearchCondition(
        List<String> categories,               // 회사명/업종 검색
        Integer minRating,            // 평점 이상
        Integer maxResponseMinutes,   // 평균 응답 시간 이하(분)
        Integer minTotalOrderCount,   // 거래 건수 이상
        Integer maxProductionHours    // 평균 제작 기간 이하(시간)
) { }
