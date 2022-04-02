package es.upm.dit.isst.followmeweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.upm.dit.isst.followmeweb.model.Pedido;
import es.upm.dit.isst.followmeweb.model.Traza;

@Controller
public class TrazaController {

    public final String TRAZAMANAGER_STRING = "http://localhost:8083/trazas/";
    public final String PEDIDOMANAGER_STRING = "http://localhost:8083/pedidos/";
    public static final String VISTA_REGISTER = "register";
    public static final String VISTA_LOGIN = "login";
    public static final String VISTA_HISTORICO = "historico";
    public static final String VISTA_MAPA = "mapa";
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

    //Modificar para que salgan pedidos, no trazas
    @GetMapping("/historico")
    public String historico(Model model) {
        List<Pedido> lista = new ArrayList<Pedido>();
        lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
        model.addAttribute("pedidos", lista);
        return VISTA_HISTORICO;
    }

    @GetMapping("/mapa/{id}")
    public String mapa(@PathVariable(value = "id") String id, Map<String, Object> model) {
        Pedido pedido = null;

                try { 
                    pedido = restTemplate.getForObject(PEDIDOMANAGER_STRING + id, Pedido.class);
                } 
                catch (HttpClientErrorException.NotFound ex) {}

                model.put("Pedido", pedido);
        return VISTA_MAPA;
    }
}
