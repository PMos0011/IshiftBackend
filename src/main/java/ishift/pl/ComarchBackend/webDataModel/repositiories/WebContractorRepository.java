package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebContractorRepository extends JpaRepository<WebContactor, Long> {

    Optional<WebContactor> findByName(String s);
}
