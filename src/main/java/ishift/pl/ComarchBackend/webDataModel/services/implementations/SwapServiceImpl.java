package ishift.pl.ComarchBackend.webDataModel.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ishift.pl.ComarchBackend.dataModel.model.BankAccount;
import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import ishift.pl.ComarchBackend.webDataModel.repositiories.SwapRepository;
import ishift.pl.ComarchBackend.webDataModel.services.SwapService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwapServiceImpl implements SwapService {

    private final SwapRepository swapRepository;

    public SwapServiceImpl(SwapRepository swapRepository) {
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

    @Override
    public void saveDeclarationData(List<DeclarationData> declarationDataList, String dbName) throws RuntimeException {

        Swap swap = swapRepository.findByDatabaseName(dbName)
                .orElse(new Swap(dbName));

        try {
            swap.setDeclarationData(new ObjectMapper().writeValueAsBytes(declarationDataList));
            swapRepository.save(swap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
