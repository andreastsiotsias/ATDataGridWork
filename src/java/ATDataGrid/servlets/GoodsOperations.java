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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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
    private String connectionURL = null;
    private String connectionUser = null;
    private String connectionPass = null;
    //
    String json_oper = null;
    String json_record = null;
    String json_accessCode = null;
    Gson gson = null;
    //
    String jsonpCallback = null;
    boolean isJSONP = false;
    //
    Enumeration<String> requestParams;
    RequestRecord reqRec = null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Get the initialisation parameters
        connectionURL = getInitParameter("connectionURL");
        connectionUser = getInitParameter("connectionUser");
        connectionPass = getInitParameter("connectionPass");
        // prepare the GSON factory
        gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        //
        try {
            // Load (and therefore register) the Sybase driver
            System.out.println("Loading driver ....");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Loaded driver !");
            System.out.println("Starting connection "+connectionURL+" ....");
            con = DriverManager.getConnection(
                    connectionURL, connectionUser, connectionPass);
            System.out.println("Connected OK !");
            System.out.println("Catalogue: "+con.getCatalog());
        }
        catch (ClassNotFoundException e) { 
            System.out.println("Could not load MySQL database driver");
        }    
        catch (SQLException e) { 
            System.out.println("Couldn't get MySQL database connection");
        }    
    }
    
    @Override
    public void destroy() {
        // Clean up.
        System.out.println("In Servlet Destroy method ....");
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
        isJSONP = false;
        jsonpCallback = request.getParameter("callback");
        if(jsonpCallback != null && !jsonpCallback.isEmpty()) {
            isJSONP = true;
            System.out.println ("Request is JSONP with callback: "+jsonpCallback);
        }
        System.out.println ("Request parameters Start");
        requestParams = request.getParameterNames();
        while (requestParams.hasMoreElements()) {
            String param = requestParams.nextElement();
            System.out.println ("--> "+param+" Value: "+request.getParameter(param));
        }
        System.out.println ("Request Parameters End");
        //
        // see what the request is
        json_oper = request.getParameter("Operation");
        // decode request from JSON to a Change Descriptor object
        json_record = request.getParameter("Record");
        reqRec = gson.fromJson(json_record, RequestRecord.class);
        //System.out.println ("<----- REQUEST START -----<");
        //System.out.println ("Received - OPERATION: "+reqRec.operation);
        //System.out.println ("Received - UID_REFERENCE: "+reqRec.uid_reference);
        //System.out.println ("Received - UNIQUE KEY: "+reqRec.uniqueKey);
        //System.out.println ("Received - RECORD: "+reqRec.record);
        //System.out.println (">----- REQUEST END ----->");
        //
        //System.out.println ("Going to sleep for 2 seconds");
        //try{Thread.sleep(2000);}catch(InterruptedException e){System.out.println(e);}
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
