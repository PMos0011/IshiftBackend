package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebCompanyDataRepository extends JpaRepository<WebCompanyData,  Long> {
}
