package hanieum.conik.application.review.required;

import hanieum.conik.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r.id FROM Review r " +
            "WHERE r.companyId = :companyId")
    Page<Long> findReviewIdsByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT r.id FROM Review r " +
            "WHERE r.member.id = :memberId")
    Page<Long> findReviewIdsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.member " +
            "WHERE r.id IN :ids")
    List<Review> findByIdsWithMember(@Param("ids") List<Long> ids);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.member " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithMember(@Param("reviewId") Long reviewId);
}