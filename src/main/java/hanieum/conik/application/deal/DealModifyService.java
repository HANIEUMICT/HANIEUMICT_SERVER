package hanieum.conik.application.deal;

import hanieum.conik.application.deal.provided.DealSaver;
import hanieum.conik.application.deal.required.DealRepository;
import hanieum.conik.domain.deal.entity.Deal;
import hanieum.conik.domain.deal.exception.DealErrorType;
import hanieum.conik.domain.deal.exception.DealException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DealModifyService implements DealSaver {

    private final DealRepository dealRepository;

    @Override
    public Deal open(Long projectId, Long buyerId) {
        Deal deal = Deal.open(projectId, buyerId);
        return dealRepository.save(deal);
    }

    @Override
    public void request(Long projectId) {
        Deal deal = dealRepository.findByProjectId(projectId)
                .orElseThrow(() -> new DealException(DealErrorType.DEAL_NOT_FOUND));

        deal.request();
    }

}
