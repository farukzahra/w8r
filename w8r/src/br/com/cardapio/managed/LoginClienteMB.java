package br.com.cardapio.managed;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import br.com.cardapio.bo.ClienteBO;
import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.bo.PermissaoBO;
import br.com.cardapio.entity.Cliente;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Lingua;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.entity.Permissao;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;
import br.com.cardapio.util.JSFHelper;
import br.com.cardapio.util.Mensagens;

@ManagedBean
@ViewScoped
public class LoginClienteMB extends BaseManagedBean<Cliente> {
    private static final long serialVersionUID = 1L;

    private Double latitude;

    private Double longitude;

    private List<Empresa> empresas;

    private Empresa empresaSel;

    private List<Mesa> mesas;

    private Mesa mesaSel;

    private boolean empresaGet;

    public LoginClienteMB() {
        String linguaLogada = (String)JSFHelper.getSession().getAttribute("LINGUA_PARAM");
        if(linguaLogada != null) {
            setLinguaLogada(Lingua.valueOf(linguaLogada));
        }
        setClazz(Cliente.class);
        // setEmpresaLogada(null);
        carregaMesas();
        HttpServletRequest request = JSFHelper.getRequest();
        if (request != null && request.getParameter("e") != null) {
            System.out.println(request.getParameter("e"));
            try {
                int codigoEmpresa = Integer.parseInt(request.getParameter("e"));
                Empresa empresa = new EmpresaBO().find(codigoEmpresa);
                if(empresa != null){
                    setEmpresaSel(empresa);
                    empresaGet = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
            }
        } else {
            System.out.println("Sem parametro");
            try {
                empresas = new EmpresaBO().buscarAtivas();
            } catch (BancoDadosException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void carregaEmpresasPerto(ActionEvent event) {
        try {
            EmpresaBO empresaBO = new EmpresaBO();
            List<Empresa> empAux = empresaBO.listByDistancia(latitude, longitude);
            if(empAux != null && !empAux.isEmpty()){
                empresas = empAux;
            }
            
            if (empresas != null && empresas.size() == 1){ 
                empresaSel = empresas.get(0);
            }
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
        }
    }

    public void carregaMesas() {
        try {
            if (empresaSel != null && empresaSel.getId() != null)
                mesas = new MesaBO().listarAtivasPorEmpresa(empresaSel.getId());
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
        }
    }

    public void actionLoginCliente() {
        try {
            if (empresaSel.isFlModuloPedido() == false || getEntity().isFlFazerPedidos() == false) {
                Cliente cliente = new Cliente();
                cliente.setEmpresaLogada(empresaSel);
                setMesaLogada(null);
                setEmpresaLogada(null);
                setClienteLogado(cliente);
                JSFHelper.redirect("cardapio.jsf?faces-redirect=true");
            } else {
                ClienteBO clienteBO = new ClienteBO();
                Cliente cliente = clienteBO.findByEmail(getEntity().getEmail());
                if (cliente == null) {
                    cliente = new Cliente();
                }
                cliente.setEmpresaLogada(empresaSel);
                setEmpresaLogada(null);
                setEntity(cliente);
                setClienteLogado(cliente);
                setMesaLogada(mesaSel);
                if (empresaSel.isTipoLoginMesa()) {
                    JSFHelper.redirect("loginmesa.jsf?faces-redirect=true");
                } else {
                    JSFHelper.redirect("loginmesacliente.jsf?faces-redirect=true");
                }
            }
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
        }
    }

    public void enviarSolicitacao() {
        try {
            Cliente cliente = getEntity();
            if (cliente.getId() == null)
                new ClienteBO().persist(cliente);
            Permissao permissao = new Permissao();
            permissao.setMesa(mesaSel);
            permissao.setCliente(cliente);
            permissao.setData(new Date());
            new PermissaoBO().persist(permissao);
            JSFHelper.redirect("cardapio.jsf?faces-redirect=true");
            cliente.setUltimaPermissao(permissao);
            setClienteLogado(cliente);
        } catch (BancoDadosException e) {
            addError(Mensagens.getMensagem(Mensagens.ERRO_AO_CARREGAR_PAGINA));
        } catch (IntegridadeReferencialException e) {
            addError(e.getMensagem());
        } catch (RegistroExistenteException e) {
            addError(e.getMensagem());
        }
    }

    public String actionLogoff() {
        setEmpresaLogada(null);
        setMesaLogada(null);
        return "logincliente.jsf?faces-redirect=true";
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public List<Mesa> getMesas() {
        if (mesas == null)
            carregaMesas();
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    public Mesa getMesaSel() {
        return mesaSel;
    }

    public void setMesaSel(Mesa mesaSel) {
        this.mesaSel = mesaSel;
        carregaMesas();
    }

    public Empresa getEmpresaSel() {
        return empresaSel;
    }

    public void setEmpresaSel(Empresa empresaSel) {
        this.empresaSel = empresaSel;
    }

    public boolean isEmpresaGet() {
        return empresaGet;
    }

    public void setEmpresaGet(boolean empresaGet) {
        this.empresaGet = empresaGet;
    }
}
