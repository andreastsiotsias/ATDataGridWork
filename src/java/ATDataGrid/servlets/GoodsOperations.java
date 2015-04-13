/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ATDataGrid.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author GB001894
 */
public class GoodsOperations extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * 
     * Had over-ridden init() method - open MySQL database connection
     * Has over-ridden destroy() method - to close mySQL database connection 
     */
    private Connection con = null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Get the initialisation parameters
        System.out.println("Connection URL: "+getInitParameter("connectionURL"));
        System.out.println("Connection User: "+getInitParameter("connectionUser"));
        System.out.println("Connection Passsword: "+getInitParameter("connectionPass"));
        //
        try {
            // Load (and therefore register) the Sybase driver
            System.out.println("Loading driver ....");
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(GoodsOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Starting connection ....");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/classicmodels", "ide", "Linden02");
        }
        catch (ClassNotFoundException e) { 
            throw new UnavailableException(this, "Couldn't load database driver");
        }    
        catch (SQLException e) { 
            throw new UnavailableException(this, "Couldn't get db connection");
        }    
    }
    
    @Override
    public void destroy() {
        // Clean up.
        try {
            if (con != null) con.close();
        }
        catch (SQLException ignored) { }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println ("Request Protocol: "+request.getProtocol());
        System.out.println ("Request Method: "+request.getMethod());
        // check if JSONP ....
        boolean isJSONP = false;
        String jsonpCallback = request.getParameter("callback");
        if(jsonpCallback != null && !jsonpCallback.isEmpty()) {
            isJSONP = true;
            System.out.println ("Request is JSONP with callback: "+jsonpCallback);
        }
        System.out.println ("Request parameters Start");
        Enumeration<String> requestParams = request.getParameterNames();
        while (requestParams.hasMoreElements()) {
            String param = requestParams.nextElement();
            System.out.println ("--> "+param+" Value: "+request.getParameter(param));
        }
        System.out.println ("Request Parameters End");
        //
        // decode change request from JSON to a Change Descriptor object
        String json_string = request.getParameter("changeRecord");
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        changeDescriptor chgReq = gson.fromJson(json_string, changeDescriptor.class);
        System.out.println ("Received - OPERATION: "+chgReq.operation);
        System.out.println ("Received - UID_REFERENCE: "+chgReq.uid_reference);
        System.out.println ("Received - UNIQUE KEY: "+chgReq.uniqueKey);
        System.out.println ("Received - RECORD: "+chgReq.record);
        //
        System.out.println ("Going to sleep for 2 seconds");
        try{Thread.sleep(2000);}catch(InterruptedException e){System.out.println(e);}
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if (isJSONP) {
                out.println(jsonpCallback+"({\"ReturnCode\": 0});");
            }
            else {
                out.println("{\"ReturnCode\": 0}");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
