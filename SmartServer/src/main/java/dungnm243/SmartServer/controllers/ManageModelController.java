package dungnm243.SmartServer.controllers;


import dungnm243.SmartServer.DAO.services.MLModelService;
import dungnm243.SmartServer.DAO.services.DatasetService;
import dungnm243.SmartServer.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ManageModelController {
    private final MLModelService mlModelService;
    private final DatasetService datasetService;

    public ManageModelController(MLModelService mlModelService, DatasetService datasetService) {
        this.mlModelService = mlModelService;
        this.datasetService = datasetService;
    }

    @GetMapping("/managemodel")
    public String showManageModelView() {
        return "managemodel";
    }

    @GetMapping("/managemodel/train/{algoType}")
    public String showDatasets(@PathVariable String algoType, Model model) {
        List<Dataset> datasets = datasetService.getAllDatasetsByType(algoType);
        model.addAttribute("datasets", datasets);
        return "choosedataset";
    }

    @PostMapping("/managemodel/train/{algoType}")
    public String submitDataset(@PathVariable String algoType, @RequestParam(name = "datasetId") Long datasetId, RedirectAttributes redirectAttributes) {
        MLModel MLModel = mlModelService.trainModel(datasetId);
        redirectAttributes.addFlashAttribute("mlModel", MLModel);
        String resultURL = "/managemodel/train/" + algoType + "/result";
        return "redirect:" + resultURL;
    }

    @GetMapping("/managemodel/train/{algoType}/result")
    public String showResultView(Model model) {
        MLModel mlModel = (MLModel) model.getAttribute("mlModel");
        model.addAttribute("mlModel", mlModel);
        return "trainresult";
    }

    @PostMapping("/managemodel/train/{algoType}/save")
    public String submitModel(@ModelAttribute MLModel mlModel) {
        mlModelService.saveModel(mlModel);
        return "redirect:/managemodel";
    }

    @GetMapping("/managemodel/choose/{algoType}")
    public String showChooseModelView(@PathVariable String algoType, Model model) {
        List<MLModel> mlModels = mlModelService.getAllModelsByType(algoType);
        model.addAttribute("algoType", algoType);
        model.addAttribute("mlModels", mlModels);
        return "choosemodel";
    }

    @PostMapping("/managemodel/choose/{algoType}")
    public String submitModel(@PathVariable String algoType, @RequestParam long selectedModelId) {
        mlModelService.makeActive(algoType, selectedModelId);
        return "redirect:/managemodel";
    }
}
