package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.Commodity;
import ishift.pl.ComarchBackend.webService.services.CommodityControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommodityController {

    private final CommodityControllerService commodityControllerService;

    public CommodityController(CommodityControllerService commodityControllerService) {
        this.commodityControllerService = commodityControllerService;
    }

    @GetMapping("/commodity/{id}")
    public ResponseEntity<List<Commodity>> getAllBankAccounts(@PathVariable String id) {

        return commodityControllerService.getAllCommodities(id);
    }

    @PutMapping("/commodity/{id}")
    public ResponseEntity<List<Commodity>> saveBankAccount(@PathVariable String id, @RequestBody Commodity commodity) {

        return commodityControllerService.saveCommodity(commodity,id);
    }

    @DeleteMapping("/commodity/{dbId}/{id}")
    public ResponseEntity<List<Commodity>> deleteBankAccount(@PathVariable String dbId, @PathVariable Long id) {

        return commodityControllerService.deleteCommodity(dbId, id);
    }
}
