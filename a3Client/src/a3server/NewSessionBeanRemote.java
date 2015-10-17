/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3server;

import javax.ejb.Remote;

/**
 *
 * @author Daniele Santoro <daniele.santoro@studenti.unitn.it>
 */
@Remote
public interface NewSessionBeanRemote {

    String method(String parameter);
    
}
