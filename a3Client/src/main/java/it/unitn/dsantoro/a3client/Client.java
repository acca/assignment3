/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.dsantoro.a3client;

import it.unitn.dsantoro.a3server.TradeRemote;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Daniele Santoro <daniele.santoro@studenti.unitn.it>
 */
public class Client {
    
    private TradeRemote trade = null;
    private InitialContext initialContext = null;
    private static final String USER_MSG = "Please tell me if you want to [S]ell, [B]uy your stocks or [Q]uit:";
    private static final float NOMINAL_PRICE = 10;
    /**
     * @param args the command line arguments
     * @throws javax.naming.NamingException
     */
    public static void main(String[] args) throws NamingException {
        Client client = new Client();        
        User user = new User();
        user.toString();
        
        System.out.println(USER_MSG);
        
        int ch;
        boolean quit = false;

        try {
            while ( ((ch = System.in.read()) != -1) && (quit == false) ) {
                System.out.println(USER_MSG);
                if (ch != '\n' && ch != '\r') {
                    switch((char)ch){
                        case 's':
                        case 'S':
                            client.sell(user);
                            break;
                        case 'b':
                        case 'B':
                            client.buy(user);
                            break;
                        case 'q':
                        case 'Q':
                            System.out.println("Quitting. Hope you had good business !!!");
                            //client.releaseRemoteTrade();
                            quit = true;
                            break;                        
                    }
                }
            
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Client() throws NamingException {
        setupRemoteTrade();
    }
    
    private void setupRemoteTrade () throws NamingException {
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
        this.initialContext = new InitialContext(jndiProps);         
        //NewSessionBeanRemote rbean = (NewSessionBeanRemote) initialContext.lookup("ejb:a3Server/a3Server-ejb/NewSessionBean!" + a3server.NewSessionBeanRemote.class.getName());
        //final Greeter bean = (Greeter) context.lookup("ejb:" + "myapp" + "/" + "myejb" + "/" + "" + "/" + "GreeterBean" + "!" + org.myapp.ejb.Greeter.class.getName());
        //NewSessionBeanRemote rbean = (NewSessionBeanRemote) initialContext.lookup("java:global/a3Server/a3Server-ejb/NewSessionBean");
        this.trade = (TradeRemote) initialContext.lookup("java:a3Server-ear-1.0-SNAPSHOT/a3Server-ejb-1.0-SNAPSHOT/Trade!it.unitn.dsantoro.a3server.TradeRemote");
    }

    private void sell(User user) {
        System.out.println("\tUser choose to sell. Default stocks amount is 10");
        int userStocks = user.getStocksAmount();
        if (userStocks < 10) {
            System.out.println("\tUser can not sell since user stocks amount is less than the minium amount for sell: " + userStocks + " stocks left.");
        }
        else {
            float tot = 0;
            for (int i=0; i<10; i++){                
                tot += this.trade.currentValue(NOMINAL_PRICE);
            }
            user.setStocksAmount(userStocks-10);
            user.setMoney(user.getMoney() + tot);
        }
        System.out.println("\t"+user);
    }

    private void buy(User user) {
        System.out.println("\tUser choose to buy. Default stocks amount is 10");
        float userMoney = user.getMoney();
        float tot = 0;
        for (int i=0; i<10; i++){                
            tot += this.trade.currentValue(NOMINAL_PRICE);
        }
        if ( userMoney <= 0 || (userMoney < tot) ) {
            System.out.println("\tUser can not buy since user money are not enough to buy a minimum amount of 10 stocks.\n" +
                    "\tUser has: "+ userMoney + " euro left.\n" + 
                    "\tTotal stocks price is: " + tot);
        }
        else {            
            user.setStocksAmount(user.getStocksAmount()+10);
            user.setMoney(userMoney - tot);
        }
        System.out.println("\t"+user);
    }

    private void releaseRemoteTrade() throws NamingException {
        this.initialContext.close();
    }
}
