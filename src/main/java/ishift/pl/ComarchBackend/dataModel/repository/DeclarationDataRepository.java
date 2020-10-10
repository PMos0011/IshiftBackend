package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DeclarationDataRepository  extends JpaRepository<DeclarationData, Long> {

    @Query("from DeclarationData where finalna = 1 and typDeklaracji < 21 and typDeklaracji <> 11 and modDate <= ?1" )
    Optional<List<DeclarationData>> findAllSupportedDeclarations(Date lastTransferDate);

}
