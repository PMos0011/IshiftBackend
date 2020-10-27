package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.Commodity;
import ishift.pl.ComarchBackend.webDataModel.model.Measure;
import ishift.pl.ComarchBackend.webService.services.CommodityControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommodityController {

    private final CommodityControllerService commodityControllerService;

    @Autowired
    public CommodityController(CommodityControllerService commodityControllerService) {
        this.commodityControllerService = commodityControllerService;
    }

    @GetMapping("/commodity/{id}")
    public ResponseEntity<List<Commodity>> getAllCommodities(@PathVariable String id) {

        return commodityControllerService.getAllCommodities(id);
    }

    @PutMapping("/commodity/{id}")
    public ResponseEntity<List<Commodity>> saveCommodity(@PathVariable String id, @RequestBody Commodity commodity) {

        return commodityControllerService.saveCommodity(commodity,id);
    }

    @DeleteMapping("/commodity/{dbId}/{id}")
    public ResponseEntity<List<Commodity>> deleteCommodity(@PathVariable String dbId, @PathVariable Long id) {

        return commodityControllerService.deleteCommodity(dbId, id);
    }

    @GetMapping("/commodity/measures/{id}")
    public ResponseEntity<List<Measure>> getAllMeasures(@PathVariable String id){

        return commodityControllerService.getAllMeasures(id);
    }
}
