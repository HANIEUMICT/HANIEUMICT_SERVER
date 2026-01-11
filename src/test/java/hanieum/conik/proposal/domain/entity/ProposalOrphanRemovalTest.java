package hanieum.conik.proposal.domain.entity;

import hanieum.conik.QueryDslTestConfig;
import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.entity.ProposalItem;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest: JPA 관련 빈(Entity, Repository, EntityManager)만 로드합니다.
 * RedisConfig, ChatController 등은 로드 범위에서 제외되므로 관련 에러가 발생하지 않습니다.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 설정을 사용하거나 H2를 자동으로 사용
@Import(QueryDslTestConfig.class)
class ProposalOrphanRemovalTest {

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Proposal 삭제 시 연관된 ProposalItem도 삭제되는지 테스트")
    void proposal_삭제시_연관된_proposalItem도_삭제되는지_테스트() {
        // given
        Proposal proposal = Proposal.create(1L, 1L, 1000000L, 500000L, 500000L, "테스트 노트", null);
        ProposalItem item1 = ProposalItem.create("아이템1", "100x100", "아이템1 노트", 500000L, 2);
        ProposalItem item2 = ProposalItem.create("아이템2", "200x200", "아이템2 노트", 250000L, 3);

        proposal.addItem(item1);
        proposal.addItem(item2);

        em.persist(proposal);
        em.flush();
        em.clear();

        // when
        Proposal foundProposal = em.find(Proposal.class, proposal.getId());
        em.remove(foundProposal);
        em.flush();
        em.clear();

        // then
        assertThat(em.find(Proposal.class, proposal.getId())).isNull();
        assertThat(em.find(ProposalItem.class, item1.getId())).isNull();
        assertThat(em.find(ProposalItem.class, item2.getId())).isNull();
    }

    @Test
    @DisplayName("Proposal에서 item 제거 시 해당 item이 삭제되는지 테스트")
    void proposal에서_item_제거시_해당_item이_삭제되는지_테스트() {
        // given
        Proposal proposal = Proposal.create(1L, 1L, 1000000L, 500000L, 500000L, "테스트 노트", null);
        ProposalItem item = ProposalItem.create("아이템", "100x100", "아이템 노트", 500000L, 2);

        proposal.addItem(item);
        em.persist(proposal);
        em.flush();
        em.clear();

        // when
        Proposal foundProposal = em.find(Proposal.class, proposal.getId());
        ProposalItem itemToRemove = foundProposal.getItems().get(0);
        foundProposal.removeItem(itemToRemove); // orphanRemoval = true 가 동작해야 함
        em.flush();
        em.clear();

        // then
        assertThat(em.find(ProposalItem.class, item.getId())).isNull();
        Proposal checkProposal = em.find(Proposal.class, proposal.getId());
        assertThat(checkProposal.getItems()).isEmpty();
    }
}