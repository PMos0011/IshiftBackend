package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SwapRepository extends JpaRepository<Swap, Long> {

    Swap findByDatabaseName(String name);

    @Query("SELECT databaseName from Swap")
    List<String> findAllDatabaseName();
}
