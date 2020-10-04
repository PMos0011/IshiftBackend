package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyDataRepository extends JpaRepository<CompanyData,Long> {

    @Query("from CompanyData where companyNumber = 1119")
    CompanyData getCompanyName();
}
