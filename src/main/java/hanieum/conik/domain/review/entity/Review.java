package hanieum.conik.domain.review.entity;

import hanieum.conik.domain.member.Member;
import hanieum.conik.domain.review.enumerate.ReviewTag;
import hanieum.conik.domain.review.exception.ReviewErrorType;
import hanieum.conik.domain.review.exception.ReviewException;
import hanieum.conik.global.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review extends AbstractEntity {
    @Column(nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 2048)
    private String content;

    @ElementCollection(targetClass = ReviewTag.class)
    @CollectionTable(
            name = "review_tags",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @Column(name = "tag")
    @Enumerated(EnumType.STRING)
    private List<ReviewTag> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @Column(name = "image_url", length = 2048)
    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false)
    private Double rating;

    private Review(Long companyId, Member member, String content, List<ReviewTag> tags, List<String> imageUrls, Double rating) {
        this.companyId = companyId;
        this.member = member;
        this.content = content;
        this.tags = (tags == null) ? new ArrayList<>() : tags;
        this.imageUrls = (imageUrls == null) ? new ArrayList<>() : imageUrls;
        this.rating = rating;
    }

    public static Review create(Long companyId, Member member, String content, List<ReviewTag> tags, List<String> imageUrls, Double rating) {
        List<ReviewTag> safeTags = (tags == null) ? new ArrayList<>() : tags;
        validateReviewData(companyId, member, content, safeTags, rating);
        return new Review(companyId, member, content, safeTags, imageUrls, rating);
    }

    public void update(String content, List<ReviewTag> tags, List<String> imageUrls, Double rating) {
        if(content != null) {
            if(content.isBlank()) throw new ReviewException(ReviewErrorType.CONTENT_IS_INVALID);
            this.content = content;
        }
        if(tags != null) {
            if(tags.size() > 3) throw new ReviewException(ReviewErrorType.TOO_MANY_TAGS);
            this.tags = tags;
        }
        if(imageUrls != null) this.imageUrls = imageUrls;
        if(rating != null) {
            if (rating < 0.0 || rating > 5.0) throw new ReviewException(ReviewErrorType.RATING_IS_INVALID);
            this.rating = rating;
        }
    }

    private static void validateReviewData(Long companyId, Member member, String content, List<ReviewTag> tags, Double rating) {
        // 유효성 검증
        if (companyId == null) throw new ReviewException(ReviewErrorType.COMPANY_IS_NULL);
        if (member == null) throw new ReviewException(ReviewErrorType.MEMBER_IS_NULL);
        if (content == null || content.isBlank()) throw new ReviewException(ReviewErrorType.CONTENT_IS_INVALID);
        if (rating == null || rating < 0.0 || rating > 5.0)
            throw new ReviewException(ReviewErrorType.RATING_IS_INVALID);
        if (tags.size() > 3) throw new ReviewException(ReviewErrorType.TOO_MANY_TAGS);
    }
}