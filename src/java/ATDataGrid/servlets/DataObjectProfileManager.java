/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ATDataGrid.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GB001894
 */
public class DataObjectProfileManager extends HttpServlet {
    
    String jsonpCallback = null;
    boolean isJSONP = false;
    Enumeration<String> requestParams;
    HttpSession session;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //
        session = request.getSession();
        // check if JSONP ....
        isJSONP = false;
        jsonpCallback = request.getParameter("callback");
        if(jsonpCallback != null && !jsonpCallback.isEmpty()) {
            isJSONP = true;
            System.out.println ("Request is JSONP with callback: "+jsonpCallback);
        }
        //
        System.out.println ("Request parameters Start");
        requestParams = request.getParameterNames();
        while (requestParams.hasMoreElements()) {
            String param = requestParams.nextElement();
            System.out.println ("--> "+param+" Value: "+request.getParameter(param));
        }
        System.out.println ("Request Parameters End");
        //
        if (request.getParameter("Operation").equals("REGISTER_AUTHENTICATION")) {
            System.out.println ("Registering Authentication");
            boolean wasRegisteredOK = registerAuthentication (
                    request.getParameter("Access_Token"),
                    request.getParameter("Issued_At"),
                    request.getParameter("Expires_At")
                    );
            if (wasRegisteredOK) {
                System.out.println ("Session was registered OK");
            }
        }
        //
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

    private boolean registerAuthentication(String access_token, String issued_at_str, String expires_at_str) {
        int issued_at = Integer.parseInt(issued_at_str);
        int expires_at = Integer.parseInt(expires_at_str);
        System.out.println ("Session ID is: "+session.getId());
        session.setAttribute("access_token", access_token);
        session.setAttribute("access_token_issued_at", issued_at);
        session.setAttribute("access_token_expires_at", expires_at);
        return true;
    }

}
