package hanieum.conik.domain.company.dto;

public record CompanySummarySearchCondition(
        String name,
        String region,
        String businessType // String 대신 Enum이면 더 안전
) {
    public static CompanySummarySearchCondition of(String name, String region, String businessType) {
        return new CompanySummarySearchCondition(
                trimToNull(name),
                trimToNull(region),
                trimToNull(businessType)
        );
    }
    private static String trimToNull(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }
}