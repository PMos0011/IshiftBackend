package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContractorControllerService {

    ResponseEntity<List<WebContactor>> getAllWebContractors(String id);

    ResponseEntity<List<WebContactor>> saveWebContractor(WebContactor webContactor, String dbId);

    ResponseEntity<List<WebContactor>> deleteWebContractor(String dbId, Long id);
}
