package PavelRudnev.CoronaTracker.controlles;


import PavelRudnev.CoronaTracker.models.LocationStats;
import PavelRudnev.CoronaTracker.service.coronaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControlles {


    @Autowired
    coronaDataService coronaDataService;
    @GetMapping("/")
    public String home (Model model) {
        model.addAttribute("locationStats",coronaDataService.getAllStats());
        return "home";
    }
}
