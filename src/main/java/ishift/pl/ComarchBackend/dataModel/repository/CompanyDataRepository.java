package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyDataRepository extends JpaRepository<CompanyData,Long> {

    @Query("from CompanyData where companyNumber = 1119")
    CompanyData getCompanyName();

    @Query("from CompanyData where id between 169 and 186 or id IN (463,1210) order by id")
    List<CompanyData> getCompanyData();

    @Query("from CompanyData where companyNumber = 1118")
    CompanyData getCompanyREGON();
}
