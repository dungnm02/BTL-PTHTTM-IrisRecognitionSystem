package dungnm243.SmartServer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class ManagerController {
    @GetMapping("/")
    public String managerView() {
        return "managerhome";
    }
}
