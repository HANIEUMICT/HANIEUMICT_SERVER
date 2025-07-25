package hanieum.conik.application.proposal.required;

import hanieum.conik.domain.proposal.domain.entity.ProposalDrawingFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalDrawingFileRepository extends JpaRepository<ProposalDrawingFile, Long> {
}
