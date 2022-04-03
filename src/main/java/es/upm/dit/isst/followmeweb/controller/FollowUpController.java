package es.upm.dit.isst.followmeweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.upm.dit.isst.followmeweb.model.Pedido;
import es.upm.dit.isst.followmeweb.model.Usuario;

@Controller
public class FollowUpController {

    public final String TRAZAMANAGER_STRING = "http://localhost:8083/trazas/";
    public final String PEDIDOMANAGER_STRING = "http://localhost:8083/pedidos/";
    public final String USUARIOMANAGER_STRING = "http://localhost:8083/usuarios/";
    public static final String VISTA_REGISTER = "register";
    public static final String VISTA_LOGIN = "login";
    public static final String VISTA_HISTORICO = "historico";
    public static final String VISTA_MAPA = "mapa";
    public static final String VISTA_INICIO = "inicio";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String inicio() {
        return "redirect:/" + VISTA_INICIO;
    }

    @GetMapping("/inicio")
    public String vistaInicio() {
        return VISTA_INICIO;
    }

    @GetMapping("/register")
    public String registrar(Map<String, Object> model) {
        Usuario usuario = new Usuario();
        model.put("Usuario", usuario);
        model.put("accion", "guardar");
        return VISTA_REGISTER;
    }

    @PostMapping("/guardar")
    public String guardar(@Validated Usuario usuario, BindingResult result, Model model) {
            if (result.hasErrors()) {
                    return VISTA_REGISTER;
            }
            try { 
                restTemplate.postForObject(USUARIOMANAGER_STRING, usuario, Usuario.class);
            } catch(Exception e) {}
            
            return "redirect:" + VISTA_HISTORICO;
        

    }

    @GetMapping("/login")
    public String vistaLogin(Map<String, Object> model) {
        //Usuario usuario = new Usuario();
        //model.put("Usuario", usuario);
        //model.put("accion", "login2");
        return VISTA_LOGIN;
    }

    @PostMapping("/login2")
    public String processLogin(@Validated Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return VISTA_LOGIN;
        }
        List<Usuario> usuarios = new ArrayList<Usuario>();
                try { 
                    usuarios = Arrays.asList(restTemplate.getForEntity(USUARIOMANAGER_STRING, Usuario[].class).getBody());
                    Map<String, String> map = new HashMap<String, String>();
                    for (Usuario u : usuarios) {
                        map.put(u.getEmail(), u.getPassword());
                    }
                    if(map.containsKey(usuario.getEmail())){
                        if(map.get(usuario.getEmail()).equals(usuario.getPassword()))
                            return "redirect:/" + VISTA_HISTORICO;
                    } 
                }
                catch (HttpClientErrorException.NotFound ex) {
                    return "redirect:/" + VISTA_LOGIN;
                }
                return "redirect:/" +  VISTA_LOGIN;
    }

    @GetMapping("/historico")
    public String historico(Model model) {
        List<Pedido> lista = new ArrayList<Pedido>();
        lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
        model.addAttribute("pedidos", lista);
        return VISTA_HISTORICO;
    }

    @GetMapping("/historico/cliente/{id}")
    public String historicoCliente(@PathVariable(value = "id") String id, Map<String, Object> model) {
        List<Pedido> lista = new ArrayList<Pedido>();
        try { 
            lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING + "cliente/" + id, Pedido[].class).getBody());
        } 
        catch (HttpClientErrorException.NotFound ex) {}

        model.put("pedidos", lista);
        return VISTA_HISTORICO;
    }

    @PostMapping("/historico/filtro")
    public String filtro(@Validated Pedido pedido, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return VISTA_REGISTER;
        }
        List<Pedido> pedidos = new ArrayList<Pedido>();
        try { 
            pedidos = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
            if(pedido.getNumeroSeguimiento()!=null || pedido.getNumeroSeguimiento() !=""){
                for(Pedido p: pedidos){
                    if(p.getNumeroSeguimiento()!=pedido.getNumeroSeguimiento())
                        pedidos.remove(p);
                }
            }
            if(String.valueOf(pedido.getIdCliente()) != null || String.valueOf(pedido.getIdCliente()) !=""){
                for(Pedido p: pedidos){
                    if(p.getIdCliente()!=pedido.getIdCliente())
                        pedidos.remove(p);
                }
            }
            if(String.valueOf(pedido.getIdVendedor()) != null || String.valueOf( pedido.getIdVendedor()) !=""){
                for(Pedido p: pedidos){
                    if(p.getIdVendedor()!= pedido.getIdVendedor())
                        pedidos.remove(p);
                }
            }
            model.addAttribute("pedidos", pedidos);
            return VISTA_HISTORICO;
        } 
        catch (HttpClientErrorException.NotFound ex) {
            return "redirect:/" + VISTA_LOGIN;
        }
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
