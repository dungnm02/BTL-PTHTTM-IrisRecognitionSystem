package dungnm243.SmartServer.DAO.services;

import dungnm243.SmartServer.DAO.repos.MLModelRepo;
import dungnm243.SmartServer.models.MLModel;
import dungnm243.SmartServer.models.Dataset;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


@Service
public class MLModelService {
    private final MLModelRepo MLModelRepo;
    private final DatasetService datasetService;
    private final WebClient client;

    public MLModelService(MLModelRepo MLModelRepo, DatasetService datasetService, WebClient client) {
        this.MLModelRepo = MLModelRepo;
        this.datasetService = datasetService;
        this.client = client;
    }

    public MLModel trainModel(long datasetId) {
        Dataset dataset = datasetService.getById(datasetId);
        String datasetPath = dataset.getDatasetPath();
        String algoType = dataset.getAlgoType();

        HashMap<String, String> req = new HashMap<>();
        req.put("datasetPath", datasetPath);
        HashMap<String, String> jsonBody =  client
                                                .post()
                                                .uri("/train/{algoType}", algoType)
                                                .bodyValue(req)
                                                .retrieve()
                                                .bodyToMono(HashMap.class)
                                                .block();

        MLModel MLModel = new MLModel();
        MLModel.setAlgoType(algoType);
        MLModel.setTrainDataset(dataset);
        MLModel.setCreateDate(LocalDate.now());
        MLModel.setModelPath(jsonBody.get("modelPath"));
        MLModel.setActive(false);
        MLModel.setAcc(Double.parseDouble(jsonBody.get("acc")));
        MLModel.setPre(Double.parseDouble(jsonBody.get("pre")));
        MLModel.setRec(Double.parseDouble(jsonBody.get("rec")));
        MLModel.setF1(Double.parseDouble(jsonBody.get("f1")));
        return MLModel;
    }

    public void saveModel(MLModel MLModel) {
        MLModelRepo.saveAndFlush(MLModel);
    }

    public List<MLModel> getAllModelsByType(String algoType) {
        return MLModelRepo.findAllByAlgoType(algoType);
    }

    public void makeActive(String algoType, long modelId) {
        List<MLModel> MLModels = MLModelRepo.findAll();
        for (MLModel MLModel : MLModels) {
            if (!MLModel.getAlgoType().equals(algoType)) {
                continue;
            }
            MLModel.setActive(MLModel.getId() == modelId);
        }
        MLModelRepo.saveAllAndFlush(MLModels);
    }

}
