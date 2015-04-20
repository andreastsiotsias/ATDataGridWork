/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ATDataGrid.servlets;

/**
 *
 * @author GB001894
 */
public class AuthorisedAgentRegister {
    // this is a singleton
    private static AuthorisedAgentRegister instance = null;
    
    protected AuthorisedAgentRegister() {
      // Exists only to defeat instantiation.
    }
    
    public static AuthorisedAgentRegister getInstance() {
        if(instance == null) {
            instance = new AuthorisedAgentRegister();
        }
        return instance;
   }
    
    public boolean registerAuthorisedAgent () {
        return true;
    }
    
    public boolean removeAuthorisedAgent (String access_key) {
        return true;
    }
    
    public boolean isAuthorisedAgent (String access_key) {
        return true;
    }
    
    public String getAuthorisedAgentID (String access_key) {
        return null;
    }
}