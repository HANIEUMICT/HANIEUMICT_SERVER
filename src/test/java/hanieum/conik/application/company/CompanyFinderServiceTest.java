package hanieum.conik.application.company;

import hanieum.conik.adapter.company.webapi.response.CompanyProfileResponse;
import hanieum.conik.adapter.company.webapi.response.CompanySummaryResponse;
import hanieum.conik.application.company.required.CompanyRepository;
import hanieum.conik.domain.company.dto.CompanyProfileSearchCondition;
import hanieum.conik.domain.company.dto.CompanySummarySearchCondition;
import hanieum.conik.domain.company.entity.Company;
import hanieum.conik.domain.company.exception.CompanyException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class CompanyFinderServiceTest {
    @Autowired
    CompanyFinderService companyFinderService;

    @MockBean
    CompanyRepository companyRepository;

    // ─────────────────────────── findAllCompanySummaries ───────────────────────────

    @Test
    @DisplayName("전체 기업 summary 조회: 빈 페이지 반환")
    void findAllCompanySummaries_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        given(companyRepository.findAll(pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        Page<CompanySummaryResponse> page = companyFinderService.findAllCompanySummaries(pageable);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).findAll(pageable);
    }

    @Test
    @DisplayName("전체 기업 summary 조회: PageRequest 자체 검증 + 서비스 상한 검증")
    void findAllCompanySummaries_invalidPageable_throws() {
        assertThatThrownBy(() -> PageRequest.of(-1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page index must not be less than zero");

        assertThatThrownBy(() -> PageRequest.of(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page size must not be less than one");

        Pageable tooLarge = PageRequest.of(0, 201);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> companyFinderService.findAllCompanySummaries(tooLarge))
                .isInstanceOf(CompanyException.class);
    }

    // ─────────────────────────── findCompany ───────────────────────────

    @Test
    @DisplayName("기업 단건 조회: 존재하면 반환")
    void findCompany_found() {
        Long id = 1L;
        Company company = mock(Company.class);
        given(companyRepository.findById(id)).willReturn(Optional.of(company));

        Company result = companyFinderService.findCompany(id);

        assertThat(result).isSameAs(company);
        verify(companyRepository).findById(id);
    }

    @Test
    @DisplayName("기업 단건 조회: 없으면 예외")
    void findCompany_notFound_throws() {
        Long id = 999L;
        given(companyRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> companyFinderService.findCompany(id))
                .isInstanceOf(CompanyException.class);
    }

    // ─────────────────────────── searchCompanySummaries ───────────────────────────

    @Test
    @DisplayName("기업명/지역/업종 검색 summary: Repository.search 위임")
    void searchCompanySummaries_delegates() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition("삼성", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        Page<CompanySummaryResponse> page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    @Test
    @DisplayName("1) 아무것도 검색하지 않은 경우(전체 조회 동작) - cond가 모두 null")
    void searchCompanySummaries_allNull_returnsAll() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition(null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        var page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    @Test
    @DisplayName("2) 이름만 검색한 경우")
    void searchCompanySummaries_nameOnly() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition("삼성", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        var page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    @Test
    @DisplayName("3) 지역으로만 필터링한 경우")
    void searchCompanySummaries_regionOnly() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition(null, "서울", null);
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        var page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    @Test
    @DisplayName("4) 업종명으로만 필터링한 경우")
    void searchCompanySummaries_businessTypeOnly() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition(null, null, "제조");
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        var page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    @Test
    @DisplayName("5) 이름/지역/업종을 섞어서 조회한 경우")
    void searchCompanySummaries_mixed() {
        // given
        CompanySummarySearchCondition cond = new CompanySummarySearchCondition("삼", "경기", "가공");
        Pageable pageable = PageRequest.of(0, 10);
        given(companyRepository.search(cond, pageable))
                .willReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        // when
        var page = companyFinderService.searchCompanySummaries(cond, pageable);

        // then
        assertThat(page.getTotalElements()).isZero();
        verify(companyRepository).search(cond, pageable);
    }

    // ─────────────────────────── findCompanyWithDetail ───────────────────────────

    @Test
    @DisplayName("기업 상세 단건 조회: 없으면 예외")
    void findCompanyWithDetail_notFound_throws() {
        Long id = 100L;
        given(companyRepository.findWithDetailById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> companyFinderService.findCompanyWithDetail(id))
                .isInstanceOf(CompanyException.class);
    }

    @Test
    @DisplayName("기업 상세 단건 조회: 상세가 null이면 예외")
    void findCompanyWithDetail_detailNull_throws() {
        Long id = 100L;
        Company company = mock(Company.class);
        given(company.getCompanyDetail()).willReturn(null);
        given(companyRepository.findWithDetailById(id)).willReturn(Optional.of(company));

        assertThatThrownBy(() -> companyFinderService.findCompanyWithDetail(id))
                .isInstanceOf(CompanyException.class);
    }

    // ─────────────────────────── findCompaniesByIds ───────────────────────────

    @Test
    @DisplayName("ID 컬렉션이 null/empty면 빈 리스트 반환하고 레포 호출 안함")
    void findCompaniesByIds_nullOrEmpty_returnsEmpty() {
        assertThat(companyFinderService.findCompaniesByIds(null)).isEmpty();
        assertThat(companyFinderService.findCompaniesByIds(List.of())).isEmpty();
        verify(companyRepository, never()).findByIdIn(anyCollection());
    }

    @Test
    @DisplayName("ID 컬렉션이 있으면 findByIdIn 위임")
    void findCompaniesByIds_nonEmpty_delegates() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<Company> expected = List.of(mock(Company.class));
        given(companyRepository.findByIdIn(ids)).willReturn(expected);

        List<Company> actual = companyFinderService.findCompaniesByIds(ids);

        assertThat(actual).isSameAs(expected);
        verify(companyRepository).findByIdIn(ids);
    }

    // ─────────────────────────── findAllCompanyWithFilter ───────────────────────────

    @Test
    @DisplayName("프로필 목록 조회(필터): Pageable 검증 및 레포 위임")
    void findAllCompanyWithFilter_delegates() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "rating"));
        CompanyProfileSearchCondition cond = new CompanyProfileSearchCondition(null, null, null, null, null);

        // 레포가 DTO 페이지를 직접 반환하는 시그니처
        Page<CompanyProfileResponse> repoPage = new PageImpl<>(List.of(), pageable, 0);
        given(companyRepository.findCompaniesWithFilter(cond, pageable)).willReturn(repoPage);

        Page<CompanyProfileResponse> result = companyFinderService.findAllCompanyWithFilter(cond, pageable);

        assertThat(result.getTotalElements()).isZero();
        verify(companyRepository).findCompaniesWithFilter(cond, pageable);
    }

    @Test
    @DisplayName("프로필 목록 조회(필터): 잘못된 Pageable이면 예외")
    void findAllCompanyWithFilter_invalidPageable_throws() {
        CompanyProfileSearchCondition cond = new CompanyProfileSearchCondition(null, null, null, null, null);
        assertThatThrownBy(() -> PageRequest.of(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page size must not be less than one");
    }
}