package br.com.cardapio.util;

import java.security.MessageDigest;

import br.com.cardapio.bo.EmpresaBO;
import br.com.cardapio.bo.MesaBO;
import br.com.cardapio.entity.Empresa;
import br.com.cardapio.entity.Mesa;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;


public class CryptMD5 {

    
	public static String encrypt(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10)
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				else
					hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
			password = hexString.toString();
		}
		catch (Exception nsae) {
			nsae.printStackTrace();
		}
		return password;
	}
	
    
	public static void resetarSenhaEmpresa(String emailEmpresa, String novaSenha) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
		EmpresaBO bo = new EmpresaBO();
		Empresa empresa = bo.findByEmail(emailEmpresa);
		empresa.setSenha(encrypt(novaSenha));
		bo.persist(empresa);
	}
	
	public static void resetarSenhaMesa(String emailEmpresa, Integer numeroMesa, String novaSenha) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException{
		MesaBO bo = new MesaBO();
		Mesa mesa = bo.buscarPorEmailEmpresaENumeroMesa(emailEmpresa, numeroMesa);
		mesa.setSenha(novaSenha);
		bo.persist(mesa);
	}
	
	public static void main(String[] args){
//		try {
			System.out.println(CryptMD5.encrypt("rama2"));
			//CryptMD5.resetarSenhaMesa("matriz@teste", 1,"1");
//		} catch (BancoDadosException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IntegridadeReferencialException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RegistroExistenteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}