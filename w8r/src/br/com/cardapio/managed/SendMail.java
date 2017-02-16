package br.com.cardapio.managed;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONObject;

@WebServlet(value = "/mail")
public class SendMail extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendMail(req, resp);
       
    }

    private void sendMail(HttpServletRequest req, HttpServletResponse resp) {
        final String username = "contato@w8r.com.br";
        final String password = "w8rmob123";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        String retorno = "0";
        try {
            // Email para nós
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contato@w8r.com.br"));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse("contato@w8r.com.br"));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("farukz@gmail.com"));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("robinsonbonilha@hotmail.com"));
            message.setSubject("Novo Cliente");
            StringBuilder text = new StringBuilder();
            text.append("Nome : " + req.getParameter("name") + "\n");
            text.append("Ramo : " + req.getParameter("atuacao") + "\n");
            text.append("Telefone : " + req.getParameter("tel") + "\n");
            text.append("E-mail : " + req.getParameter("email") + "\n");
            text.append("Mensagem : " + req.getParameter("message") + "\n");
            message.setText(text.toString());
            Transport.send(message);
            
            // Email de confirmação
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contato@w8r.com.br"));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(req.getParameter("email")));
            message.setSubject("[W8R] Contato - Mensagem Automática");
            text = new StringBuilder();
            text.append("Obrigado pelo contato, em breve um representante comercial entrará em contato com você.");
            message.setText(text.toString());
            Transport.send(message);
            
            
            retorno = "1";
            System.out.println("Email enviado de : " + req.getParameter("email"));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            resp.setContentType("application/json");
            JSONObject JObject = new JSONObject(); 
            JObject.put("sucess", retorno); 
            PrintWriter out = resp.getWriter();
            out.print(JObject);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
    }
}
