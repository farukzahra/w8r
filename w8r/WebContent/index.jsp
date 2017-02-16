<% 
String l = request.getParameter("l");
if(l != null) {
    session.setAttribute("LINGUA_PARAM", l);
}
else {
    session.setAttribute("LINGUA_PARAM", "PT");
}

response.sendRedirect("pages/logincliente.jsf"); 

%>