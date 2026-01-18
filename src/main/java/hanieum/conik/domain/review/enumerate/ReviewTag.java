package hanieum.conik.domain.review.enumerate;

public enum ReviewTag {
    FAST_RESPONSE("답변 속도가 빨랐어요"),
    REASONABLE_PRICE("합리적인 가격이었어요"),
    HIGH_QUALITY("결과물 퀄리티가 좋아요"),
    FAST_PRODUCTION("제작 기간이 빨랐어요"),
    TRUSTWORTHY("믿을만한 거래처였어요");

    private final String description;

    ReviewTag(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}