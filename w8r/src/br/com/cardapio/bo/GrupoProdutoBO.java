package br.com.cardapio.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.GrupoProduto;
import br.com.cardapio.entity.Produto;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.util.Utils;

public class GrupoProdutoBO extends BOBase<GrupoProduto> {
    
    private static final long serialVersionUID = 1L;

    public GrupoProdutoBO() {
        super();
        setClazz(GrupoProduto.class);
    }
    
    public void importarGrupoEProdutos(InputStream inputStream, Empresa empresa) throws Exception{        
        List<String[]> dados = extraiDadosArquivo(inputStream);
        HashMap<String, GrupoProduto> hashGP = persisteGrupoProduto(dados, empresa);
        new ProdutoBO().persisteProdutoBatch(hashGP, dados);
    }
    
    private HashMap<String, GrupoProduto> persisteGrupoProduto(List<String[]> dados, Empresa empresa) throws Exception {
        HashMap<String, GrupoProduto> hashGP = new HashMap<String, GrupoProduto>();
        for (String[] s : dados) {
            if (s.length == 8) {
                GrupoProduto gp = new GrupoProduto(s[0], Integer.parseInt(s[1]), empresa);
                GrupoProduto grupoProduto = hashGP.get(gp.getNome());
                if (grupoProduto == null) {
                    hashGP.put(gp.getNome(), gp);
                    grupoProduto = gp;
                }
            } else {
                System.out.println(s[3] + " ==============>>>>>>   esta com erro tem " + s.length + " colunas");
            }
        }
        persistBatch(new ArrayList<GrupoProduto>(hashGP.values()));
        List<GrupoProduto> listarPorEmpresaEAtivo = listarPorEmpresaEAtivo(empresa.getId());
        hashGP = new HashMap<String, GrupoProduto>();
        for (GrupoProduto gp : listarPorEmpresaEAtivo) {
            hashGP.put(gp.getNome(), gp);
        }
        return hashGP;
    }

    public List<String[]> extraiDadosArquivo(InputStream inputStream) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String[]> dados = new ArrayList<String[]>();
        while ((line = br.readLine()) != null) {
            if (line != null && !line.isEmpty()) {
                String[] s = line.split(";");
                dados.add(s);
            }
        }   
        return dados;
    }
    
    public static void main(String[] args) {
        try {
            FileInputStream inputStream = new FileInputStream(new File("E:\\Faruk\\workspace\\w8r\\dados.txt"));
            Empresa empresa = new EmpresaBO().find(166);
            new GrupoProdutoBO().importarGrupoEProdutos(inputStream, empresa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<GrupoProduto> listarPorStatus(boolean apenasAtivas) throws BancoDadosException{  
    	Map<String, Object> parametros = new HashMap<String, Object>();
        if (apenasAtivas) {            	
        	parametros.put("status", 'A');
        }
        String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao);       
    }
    
    public List<GrupoProduto> listarPorEmpresaEAtivo(Integer idEmpresa) throws BancoDadosException{  
    	Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        parametros.put("status", 'A');
        String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao);        
    }
    
    public List<GrupoProduto> listarPorEmpresa(Integer idEmpresa) throws BancoDadosException{  
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("empresa.id", idEmpresa);
        String[] ordenacao = new String[]{"ordenacao","nome"};
        return listByFields(parametros, ordenacao);       
    }
    
}