package es.upm.dit.isst.followmeweb.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import es.upm.dit.isst.followmeweb.model.Traza;

@Controller
public class TrazaController {

    public final String TFGMANAGER_STRING= "http://localhost:8083/trazas/";
    public static final String VISTA_REGISTER = "register";
    public static final String VISTA_LOGIN = "login";
    public static final String VISTA_HISTORICO = "historico";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String inicio() {
        return "redirect:/" + VISTA_LOGIN;
    }

    @GetMapping("/register")
    public String lista() {
        return VISTA_REGISTER;
    }

    @GetMapping("/login")
    public String vistaLogin() {
        return VISTA_LOGIN;
    }

    @GetMapping("/historico")
    public String historico(Model model) {
        List<Traza> lista = new ArrayList<Traza>();
        model.addAttribute("trazas", lista);
        return VISTA_HISTORICO;
    }
}
