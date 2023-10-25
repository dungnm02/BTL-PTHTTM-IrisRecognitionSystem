package dungnm243.SmartServer.DAO.services;

import dungnm243.SmartServer.DAO.repos.DatasetRepo;
import dungnm243.SmartServer.models.Dataset;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DatasetService {
    private final WebClient localApiClient;
    private final DatasetRepo datasetRepo;

    public DatasetService(WebClient localApiClient, DatasetRepo datasetRepo) {
        this.datasetRepo = datasetRepo;
        this.localApiClient = localApiClient;
    }

    public Dataset getById(Long id) {
        return datasetRepo.findById(id).orElse(null);
    }

    public List<Dataset> getAllDatasetsByType(String algoType) {
        return datasetRepo.findAllByAlgoType(algoType);
    }

}
