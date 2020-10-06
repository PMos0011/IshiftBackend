package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebCompanyDataRepository extends JpaRepository<WebCompanyData,  Long> {

    List<WebCompanyData> findAllByOfficeID(String id);

    WebCompanyData findByRandomId(String id);
}
