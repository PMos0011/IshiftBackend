package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeclarationDataRepository  extends JpaRepository<DeclarationData, Long> {

    @Query("from DeclarationData where finalna = 1")
    List<DeclarationData> findAll();
}
