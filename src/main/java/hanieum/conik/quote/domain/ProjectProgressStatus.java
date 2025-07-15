package hanieum.conik.quote.domain;

public enum ProjectProgressStatus {

    BEFORE_BIDDING("입찰 전"),
    BIDDING_IN_PROGRESS("입찰 중"),
    BIDDING_COMPLETED("입찰 완료");

    private final String description;

    ProjectProgressStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
