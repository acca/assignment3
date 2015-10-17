/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3server;

import javax.ejb.Stateless;

/**
 *
 * @author Daniele Santoro <daniele.santoro@studenti.unitn.it>
 */
@Stateless
public class NewSessionBean implements NewSessionBeanRemote {

    public String method(String parameter) {
        System.out.println("method()");
        return "Hello from remote method";        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
