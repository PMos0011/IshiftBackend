package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SwapRepository extends JpaRepository<Swap, Long> {

    Optional<Swap> findByDatabaseName(String name);

    @Query("SELECT databaseName from Swap")
    List<String> findAllDatabaseName();
}
