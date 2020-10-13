package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.dataModel.model.Contractor;
import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;

import java.util.List;

public interface WebContractorService {

    List<WebContactor> convertFromContractorListToWebContractorListAndSave(List<Contractor> contractorList);
 }
