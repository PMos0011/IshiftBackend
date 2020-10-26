package ishift.pl.ComarchBackend.databaseService.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.dataModel.repository.*;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import ishift.pl.ComarchBackend.webDataModel.model.Measure;
import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import ishift.pl.ComarchBackend.webDataModel.repositiories.InvoiceTypeRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.MeasureRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.SwapRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webDataModel.services.BankAccountDataService;
import ishift.pl.ComarchBackend.webDataModel.services.WebContractorService;
import ishift.pl.ComarchBackend.webDataModel.services.WebInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BootstrapFromDB implements CommandLineRunner {

    private final SwapRepository swapRepository;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final CompanyDataRepository companyDataRepository;
    private final BankAccountDataService bankAccountDataService;
    private final WebContractorService webContractorService;
    private final WebInvoiceService webInvoiceService;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final MeasureRepository measureRepository;

    @Autowired
    public BootstrapFromDB(SwapRepository swapRepository, DeclarationDataRepository declarationDataRepository,
                           DeclarationDetailsRepository declarationDetailsRepository,
                           WebCompanyDataRepository webCompanyDataRepository,
                           DataBaseAccess dataBaseAccess,
                           CompanyDataRepository companyDataRepository,
                           BankAccountDataService bankAccountDataService,
                           WebContractorService webContractorService,
                           WebInvoiceService webInvoiceService,
                           InvoiceTypeRepository invoiceTypeRepository,
                           MeasureRepository measureRepository) {
        this.swapRepository = swapRepository;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.companyDataRepository = companyDataRepository;
        this.bankAccountDataService = bankAccountDataService;
        this.webContractorService = webContractorService;
        this.webInvoiceService = webInvoiceService;
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.measureRepository = measureRepository;

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println(dataBasesPairListSingleton.getDatabasesList());
        System.out.println("from database-service");

        saveDataToDatabase();

    }

    void saveDataToDatabase() {

        List<Swap> swapList = swapRepository.findAll();

        if (swapList.size() > 0) {
            swapList.forEach(swap -> {

                Optional<String> existingDB = dataBasesListSingleton.getDatabasesList().stream()
                        .filter(existingName -> swap.getDatabaseName().equals(existingName))
                        .findAny();

                existingDB.map(name -> {
                    ClientDatabaseContextHolder.set(name);
                    System.out.println("kopiuję");
                    System.out.println(name);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        TransferObject transferObject = objectMapper.readValue(
                                swap.getCustomerData(), new TypeReference<>() {
                                });

                        companyDataRepository.saveAll(transferObject.getCompanyData());

                        bankAccountDataService.convertBankAccountListToBankDataAccountListAndSave(transferObject.getBankAccounts());

                        List<DeclarationData> declarationDataList = objectMapper.readValue(
                                swap.getDeclarationData(), new TypeReference<>() {
                                });

                        declarationDataList.forEach(doc -> {
                            declarationDetailsRepository.saveAll(doc.getDeclarationDetails());
                            declarationDataRepository.save(doc);
                        });

                        webContractorService.convertFromContractorListToWebContractorListAndSave(transferObject.getContractorList());
                        webInvoiceService.convertInvoiceListToWebInvoiceListAndSave(transferObject.getInvoiceList());

                        setInvoiceTypes();
                        setMeasureUnits();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ClientDatabaseContextHolder.clear();
                    swapRepository.delete(swap);
                    return name;

                }).orElseGet(() -> {
                    swapRepository.delete(swap);
                    //TODO
                    //error handling
                    System.out.println("error getting data from swap");
                    return null;
                });
            });
        }
    }

    private void setInvoiceTypes() {
        List<InvoiceType> invoiceTypeList = new ArrayList<>();

        invoiceTypeList.add(new InvoiceType("Faktura sprzedaży", "num/mm/yyyy", "FS"));
        invoiceTypeList.add(new InvoiceType("Faktura korygująca", "num/mm/yyyy", "FKOR"));
        invoiceTypeList.add(new InvoiceType("Faktura zaliczkowa", "num/mm/yyyy", "FAZL"));

        invoiceTypeRepository.saveAll(invoiceTypeList);
    }

    private void setMeasureUnits() {
        List<Measure> measureList = new ArrayList<>();

        measureList.add(new Measure("szt."));
        measureList.add(new Measure("godz."));
        measureList.add(new Measure("usł."));
        measureList.add(new Measure("doba"));
        measureList.add(new Measure("dzień"));
        measureList.add(new Measure("gr"));
        measureList.add(new Measure("grupa"));
        measureList.add(new Measure("h"));
        measureList.add(new Measure("kg"));
        measureList.add(new Measure("km"));
        measureList.add(new Measure("kpl."));
        measureList.add(new Measure("kurs"));
        measureList.add(new Measure("l"));
        measureList.add(new Measure("m"));
        measureList.add(new Measure("m2"));
        measureList.add(new Measure("m3"));
        measureList.add(new Measure("mb"));
        measureList.add(new Measure("mies"));
        measureList.add(new Measure("opak."));
        measureList.add(new Measure("pkt."));
        measureList.add(new Measure("rolka"));
        measureList.add(new Measure("strona"));
        measureList.add(new Measure("rbg"));
        measureList.add(new Measure("Mg"));
        measureList.add(new Measure("cm"));
        measureList.add(new Measure("abonament"));

        measureRepository.saveAll(measureList);

    }

}
