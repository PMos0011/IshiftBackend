package ishift.pl.ComarchBackend.webDataModel.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import ishift.pl.ComarchBackend.webDataModel.repositiories.SwapRepository;
import org.springframework.stereotype.Service;

@Service
public class SwapDataServiceImpl  implements SwapDataService{

    private final SwapRepository swapRepository;

    public SwapDataServiceImpl(SwapRepository swapRepository) {
        this.swapRepository = swapRepository;
    }

    @Override
    public void saveCompanyData(TransferObject transferObject) {
        Swap swap = new Swap();
        swap.setDatabaseName(transferObject.getDbName());

        try {
            swap.setCustomerData(new ObjectMapper().writeValueAsBytes(transferObject));
            swapRepository.save(swap);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
