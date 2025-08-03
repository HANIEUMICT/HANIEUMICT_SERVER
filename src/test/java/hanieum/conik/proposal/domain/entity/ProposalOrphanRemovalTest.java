package hanieum.conik.proposal.domain.entity;

import hanieum.conik.domain.proposal.domain.entity.Proposal;
import hanieum.conik.domain.proposal.domain.entity.ProposalItem;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProposalOrphanRemovalTest {
    @Autowired
    private EntityManager em;

    @Test
    void proposal_삭제시_연관된_proposalItem도_삭제되는지_테스트() {
        // given
        // Proposal 생성
        Proposal proposal = Proposal.create(
                1L,                 // projectId
                1L,                 // companyId
                1000000L,          // totalPrice
                500000L,           // firstPrice
                500000L,           // secondPrice
                "테스트 노트"       // proposalNote
        );

        // ProposalItem 생성 및 연결
        ProposalItem item1 = ProposalItem.create(
                "아이템1",
                "100x100",     // itemSize
                "아이템1 노트",  // itemNote
                500000L,       // itemUnitPrice
                2             // itemQuantity
        );

        ProposalItem item2 = ProposalItem.create(
                "아이템2",
                "200x200",     // itemSize
                "아이템2 노트",  // itemNote
                250000L,       // itemUnitPrice
                3             // itemQuantity
        );

        // 양방향 연관관계 설정
        proposal.addItem(item1);
        proposal.addItem(item2);

        // 영속성 컨텍스트에 저장
        em.persist(proposal);
        em.flush();
        em.clear();

        // when
        // proposal 조회 후 삭제
        Proposal foundProposal = em.find(Proposal.class, proposal.getId());
        em.remove(foundProposal);
        em.flush();
        em.clear();

        // then
        // proposal과 proposalItem 모두 삭제되었는지 확인
        Proposal deletedProposal = em.find(Proposal.class, proposal.getId());
        ProposalItem deletedItem1 = em.find(ProposalItem.class, item1.getId());
        ProposalItem deletedItem2 = em.find(ProposalItem.class, item2.getId());

        assertThat(deletedProposal).isNull();
        assertThat(deletedItem1).isNull();
        assertThat(deletedItem2).isNull();
    }

    @Test
    void proposal에서_item_제거시_해당_item이_삭제되는지_테스트() {
        // given
        Proposal proposal = Proposal.create(
                1L, 1L, 1000000L, 500000L,
                500000L,  "테스트 노트"
        );

        ProposalItem item = ProposalItem.create(
                "아이템 노트",
                "100x100",
                "아이템 노트",
                500000L,
                2
        );

        proposal.addItem(item);
        em.persist(proposal);
        em.flush();
        em.clear();

        // when
        Proposal foundProposal = em.find(Proposal.class, proposal.getId());
        ProposalItem itemToRemove = foundProposal.getItems().get(0);
        foundProposal.removeItem(itemToRemove);
        em.flush();
        em.clear();

        // then
        ProposalItem deletedItem = em.find(ProposalItem.class, item.getId());
        assertThat(deletedItem).isNull();

        Proposal checkProposal = em.find(Proposal.class, proposal.getId());
        assertThat(checkProposal.getItems()).isEmpty();
    }
}