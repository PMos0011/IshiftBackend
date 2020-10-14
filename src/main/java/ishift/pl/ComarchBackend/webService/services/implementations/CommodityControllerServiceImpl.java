package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.Commodity;
import ishift.pl.ComarchBackend.webDataModel.repositiories.CommodityRepository;
import ishift.pl.ComarchBackend.webService.services.CommodityControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityControllerServiceImpl implements CommodityControllerService {

    private final CommodityRepository commodityRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    @Autowired
    public CommodityControllerServiceImpl(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
    }
    @Override
    public ResponseEntity<List<Commodity>> getAllCommodities(String id) {
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<Commodity> commodities = commodityRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>(commodities, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Commodity>> saveCommodity(Commodity commodity, String dbId) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(dbId));
        commodityRepository.save(commodity);
        ClientDatabaseContextHolder.clear();

        return getAllCommodities(dbId);
    }

    @Override
    public ResponseEntity<List<Commodity>> deleteCommodity(String dbId, Long id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(dbId));
        commodityRepository.deleteById(id);
        ClientDatabaseContextHolder.clear();

        return getAllCommodities(dbId);
    }
}
