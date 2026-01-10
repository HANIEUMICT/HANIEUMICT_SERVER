package hanieum.conik.application.deal.provided;

import hanieum.conik.domain.deal.entity.Deal;

public interface DealSaver {

    Deal open(Long projectId, Long buyerId);

    void request(Long projectId);

}
