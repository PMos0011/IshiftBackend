package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.model.Commodity;
import ishift.pl.ComarchBackend.webDataModel.model.Measure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommodityControllerService {

    ResponseEntity<List<Commodity>> getAllCommodities(String id);

    ResponseEntity<List<Commodity>> saveCommodity(Commodity commodity, String dbId);

    ResponseEntity<List<Commodity>> deleteCommodity(String dbId, Long id);

    ResponseEntity<List<Measure>> getAllMeasures();

}
