/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.dsantoro.a3client;

import it.unitn.dsantoro.a3server.TradeRemote;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Daniele Santoro <daniele.santoro@studenti.unitn.it>
 */
public class Client {
/**
     * @param args the command line arguments
     * @throws javax.naming.NamingException
     */
    public static void main(String[] args) throws NamingException {
        // TODO code application logic here
        Client client = new Client();
    }
    public Client() throws NamingException {
        
        
        
        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        //jndiProps.put(Context.PROVIDER_URL, "remote://127.0.0.1:8080");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
        //jndiProps.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
        
        
        //This property is important for remote resolving
        jndiProps.put("jboss.naming.client.ejb.context", true);
        //This propert is not important for remote resolving
        jndiProps.put("org.jboss.ejb.client.scoped.context", true);
    
        // username
        jndiProps.put(Context.SECURITY_PRINCIPAL, "user");
        // password
        jndiProps.put(Context.SECURITY_CREDENTIALS, "pw");
        InitialContext initialContext = new InitialContext(jndiProps);
        System.out.println(initialContext);
        TradeRemote trade = (TradeRemote) initialContext.lookup("java:a3Server-ear-1.0-SNAPSHOT/a3Server-ejb-1.0-SNAPSHOT/Trade!it.unitn.dsantoro.a3server.TradeRemote");
        //NewSessionBeanRemote rbean = (NewSessionBeanRemote) initialContext.lookup("ejb:a3Server/a3Server-ejb/NewSessionBean!" + a3server.NewSessionBeanRemote.class.getName());
        //final Greeter bean = (Greeter) context.lookup("ejb:" + "myapp" + "/" + "myejb" + "/" + "" + "/" + "GreeterBean" + "!" + org.myapp.ejb.Greeter.class.getName());
        //NewSessionBeanRemote rbean = (NewSessionBeanRemote) initialContext.lookup("java:global/a3Server/a3Server-ejb/NewSessionBean");
        System.out.println(trade.currentValue(10));
        
        
        //initialContext.close();
}    
}
