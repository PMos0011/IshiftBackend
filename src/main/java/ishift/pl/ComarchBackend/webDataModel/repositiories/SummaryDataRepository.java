package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.SummaryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryDataRepository extends JpaRepository<SummaryData,Long> {
}
