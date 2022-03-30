package es.upm.dit.isst.followmeweb.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import es.upm.dit.isst.followmeweb.model.Traza;

@Controller
public class TrazaController {
    
    public final String TRAZAMANAGER_STRING= "http://localhost:8083/trazas/";
    public static final String VISTA_LOGIN = "login";
    public static final String VISTA_LISTA = "lista";
    public static final String VISTA_MAPA = "mapa";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String inicio() {
        return "redirect:/" + VISTA_LOGIN;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/" + VISTA_LOGIN;
    }

    /*@GetMapping("/lista")
    public String lista(Model model, Principal principal) {
        List<String> lista = new ArrayList<String>();
        lista = Arrays.asList(restTemplate.getForEntity(TRAZAMANAGER_STRING, String[].class).getBody());
        model.addAttribute("pedidos", lista);
        return VISTA_LISTA;
    }*/


}
