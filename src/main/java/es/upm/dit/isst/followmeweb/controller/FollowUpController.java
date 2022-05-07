package es.upm.dit.isst.followmeweb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public static final String VISTA_USUARIOS = "usuarios";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String inicio() {
        return "redirect:/" + VISTA_INICIO;
    }

    @GetMapping("/inicio")
    public String vistaInicio(@AuthenticationPrincipal Usuario usuario) {
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
            if (usuario.getRol() == null) {
                usuario.setRol("ROLE_CLI");
            }
            usuario.setPassword(BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()));
            restTemplate.postForObject(USUARIOMANAGER_STRING, usuario, Usuario.class);
        } catch (Exception e) {
        }
        return "redirect:" + VISTA_INICIO;
    }

    @GetMapping("/login")
    public String vistaLogin(Map<String, Object> model) {
        return VISTA_LOGIN;
    }

    @GetMapping("/historico")
    public String historico(Model model,
            @RequestParam(name = "numeroSeguimiento", required = false, defaultValue = "") String numeroSeguimiento,
            Authentication auth) {
        List<Pedido> lista = new ArrayList<Pedido>();
        model.addAttribute("numeroSeguimiento", numeroSeguimiento);
        try {
            /*
             * Pedido pedido = restTemplate.getForObject(PEDIDOMANAGER_STRING + "cliente/" +
             * auth.getName(), Pedido.class);
             * if (pedido != null)
             * lista.add(pedido);
             */
            // Mostrar la lista de pedidos segun el usuario registrado
            for (GrantedAuthority rol : auth.getAuthorities()) {
                if ("ROLE_CLI".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "cliente/" + auth.getName(), Pedido[].class)
                            .getBody());
                    break;
                } else if ("ROLE_EMP".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "vendedor/" + auth.getName(), Pedido[].class)
                            .getBody());
                    break;
                } else if ("ROLE_REP".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "repartidor/" + auth.getName(), Pedido[].class)
                            .getBody());
                    break;
                } else if ("ROLE_ADM".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
                    break;
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
        }
        model.addAttribute("pedidos", lista);
        return VISTA_HISTORICO;
    }



    @GetMapping("/mapa/{id}")
    public String mapa(@PathVariable(value = "id") String id, Map<String, Object> model) {
        Pedido pedido = null;
        try {
            pedido = restTemplate.getForObject(PEDIDOMANAGER_STRING + id, Pedido.class);
            if(pedido.getVehiculo().equals(null)){
                pedido.setVehiculo("coche");
            }
        } catch (HttpClientErrorException.NotFound ex) {
        }
        model.put("Pedido", pedido);
        return VISTA_MAPA;
    }

    @PostMapping("/historico/filtro")
    public String filtro(Model model, @RequestParam(name = "numeroSeguimiento", required = false, defaultValue = "") String numeroSeguimiento,
    Authentication auth) {
        List<Pedido> lista = new ArrayList<Pedido>();
        List<Pedido> filtrada = new ArrayList<Pedido>();
        model.addAttribute("numeroSeguimiento", numeroSeguimiento);
        try {
            // Mostrar la lista de pedidos segun el usuario registrado
            for (GrantedAuthority rol : auth.getAuthorities()) {
                if ("ROLE_CLI".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "cliente/" + auth.getName(), Pedido[].class)
                            .getBody());
                    for(Pedido ped: lista){
                        if(ped.getNumeroSeguimiento().equals(numeroSeguimiento)){
                            filtrada.add(ped);
                            break;
                        }
                    }
                    break;
                } else if ("ROLE_EMP".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "vendedor/" + auth.getName(), Pedido[].class)
                            .getBody());

                            for(Pedido ped: lista){
                                if(ped.getNumeroSeguimiento().equals(numeroSeguimiento)){
                                    filtrada.add(ped);
                                    break;
                                }
                            }
                    break;
                } else if ("ROLE_REP".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate
                            .getForEntity(PEDIDOMANAGER_STRING + "repartidor/" + auth.getName(), Pedido[].class)
                            .getBody());

                            for(Pedido ped: lista){
                                if(ped.getNumeroSeguimiento().equals(numeroSeguimiento)){
                                    filtrada.add(ped);
                                    break;
                                }
                            }
                    break;
                } else if ("ROLE_ADM".equals(rol.getAuthority())) {
                    lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
                    for(Pedido ped: lista){
                        if(ped.getNumeroSeguimiento().equals(numeroSeguimiento)){
                            filtrada.add(ped);
                            break;
                        }
                    }
                    break;
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            lista = Arrays.asList(restTemplate.getForEntity(PEDIDOMANAGER_STRING, Pedido[].class).getBody());
        }
        model.addAttribute("pedidos", filtrada);
        return VISTA_HISTORICO;
    }


    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        List<Usuario> lista = new ArrayList<Usuario>();
        try {
            lista = Arrays.asList(restTemplate.getForEntity(USUARIOMANAGER_STRING, Usuario[].class).getBody());
        } catch (Exception e) {
        }
        model.addAttribute("usuarios", lista);
        return VISTA_USUARIOS;
    }

    @GetMapping("/eliminar")
    public String eliminar(@RequestParam int usuarioId) {
        restTemplate.delete(USUARIOMANAGER_STRING+ usuarioId);
        return "redirect:/" + VISTA_USUARIOS;
    }

    @PostMapping("/usuarios/filtro")
    public String usuariosFiltro (Model model, @RequestParam String username) {
        List<Usuario> lista = new ArrayList<Usuario>();
        List<Usuario> filtrada = new ArrayList<Usuario>();
        try {
            lista = Arrays.asList(restTemplate.getForEntity(USUARIOMANAGER_STRING, Usuario[].class).getBody());
            for(Usuario user: lista){
                if(user.getUsername().equals(username)){
                    filtrada.add(user);
                    break;
                }
            }
        } catch (Exception e) {
        }
        model.addAttribute("usuarios", filtrada);
        return VISTA_USUARIOS;
    }


    @GetMapping("/estado/{id}") 
    public String cambiarEstado(@PathVariable(value = "id") String id, @RequestParam String estado) {
        Pedido pedido = null;
        try{
            pedido = restTemplate.getForObject(PEDIDOMANAGER_STRING + id, Pedido.class);
            pedido.setEstado(Integer.parseInt(estado));
            restTemplate.put(PEDIDOMANAGER_STRING + pedido.getNumeroSeguimiento(), pedido, Pedido.class);
        }catch(Exception e){
            System.out.println("error");
        }
        return "redirect:/" + VISTA_HISTORICO;
    }

    @GetMapping("/habilitar/{id}") 
    public String habilitarUsuario(@PathVariable(value = "id") String id) {
        Usuario usuario = null;
        try{
            usuario = restTemplate.getForObject(USUARIOMANAGER_STRING + id, Usuario.class);
            if(usuario.isEnable()){
                usuario.setEnable(false);
            }else{
                usuario.setEnable(true);
            }
            restTemplate.put(USUARIOMANAGER_STRING + usuario.getId(), usuario, Usuario.class);
        }catch(Exception e){
            System.out.println("error");
        }
        return "redirect:/" + VISTA_USUARIOS;
    }

    @GetMapping("/vehiculo/{id}") 
    public String asignarVehiculo(@PathVariable(value = "id") String id, @RequestParam String vehiculo) {
        Pedido pedido = null;
        try{
            pedido = restTemplate.getForObject(PEDIDOMANAGER_STRING + id, Pedido.class);
            pedido.setVehiculo(vehiculo);
            restTemplate.put(PEDIDOMANAGER_STRING + pedido.getNumeroSeguimiento(), pedido, Pedido.class);
        }catch(Exception e){
            System.out.println("error");
        }
        return "redirect:/" + VISTA_HISTORICO;
    }

}
