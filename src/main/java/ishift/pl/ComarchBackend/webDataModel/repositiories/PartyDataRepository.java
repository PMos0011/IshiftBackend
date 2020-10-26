package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.PartyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyDataRepository extends JpaRepository<PartyData, Long> {
}
