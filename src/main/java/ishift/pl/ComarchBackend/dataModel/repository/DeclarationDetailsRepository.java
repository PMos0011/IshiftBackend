package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeclarationDetailsRepository extends JpaRepository<DeclarationDetails, Long> {
}
