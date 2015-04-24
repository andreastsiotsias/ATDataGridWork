/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ATDataGrid.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author GB001894
 */
public class DataObjectProfileManager extends HttpServlet {
    
    String jsonpCallback = null;
    boolean isJSONP = false;
    Enumeration<String> requestParams;
    

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
        if (request.getParameter("Operation").equals("REGISTER_AUTHENTICATION")) {
            System.out.println ("Registering Authentication");
            registerAuthentication (request, response);
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

    private void registerAuthentication(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String access_token = request.getParameter("Access_Token");
        int issued_at = Integer.parseInt(request.getParameter("Issued_At"));
        int expires_at = Integer.parseInt(request.getParameter("Expires_At"));
        boolean isValidAccessToken = false;
        try {
            isValidAccessToken = doAccessTokenValidation (session, access_token, issued_at, expires_at);
        }
        catch (Exception e) {
            //
        }
        if (isValidAccessToken) {
            System.out.println ("Session ID is: "+session.getId());
            System.out.println ("Access Token Validated");
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("{\"ReturnCode\": 0}");
            } catch (IOException ex) {
                Logger.getLogger(DataObjectProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            System.out.println ("Access Token Is Not Valid");
            response.setStatus(407);
            try (PrintWriter out = response.getWriter()) {
                out.println("{\"ReturnCode\": 407}");
            } catch (IOException ex) {
                Logger.getLogger(DataObjectProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean doAccessTokenValidation (HttpSession session, String access_token, int issued_at, int expires_at) throws Exception {
 	//String access_token="ya29.XgHOFfMKYSSkT6oePTOP8PHuoa8vFlEm65Y3mmIJAgwhTBNL6zEQva2RFMIOl_joxzn2j_L-Jt6HbA";
        String USER_AGENT = "Mozilla/5.0";
        String client_id = "815038451936-611r1ll7e9tkdl1kvhhvc9dokp5e9176.apps.googleusercontent.com";
        String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token="+access_token;
 	HttpClient client = HttpClientBuilder.create().build();
	HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        System.out.println("Sending 'GET' request to URL : " + url);
	HttpResponse response = client.execute(request);
        int reqRC = response.getStatusLine().getStatusCode();
	System.out.println("Response Code : " + reqRC);
        if (reqRC != 200) return false;
	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
            result.append(line);
	}
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        GooglePlusSignInTokenInfo reqRec = gson.fromJson(result.toString(), GooglePlusSignInTokenInfo.class);
        System.out.println ("Token issued to : "+reqRec.issued_to);
        System.out.println ("Token audience : "+reqRec.audience);
        System.out.println ("Token user id : "+reqRec.user_id);
        System.out.println ("Token scope : "+reqRec.scope);
        System.out.println ("Token expires in : "+reqRec.expires_in+" seconds");
        System.out.println ("Token email : "+reqRec.email);
        System.out.println ("Token verified_email : "+reqRec.verified_email);
        System.out.println ("Token access type : "+reqRec.access_type);
        if (!client_id.equals(reqRec.audience)) return false;
        if (reqRec.expires_in < 1) return false;
        session.setAttribute("access_token", access_token);
        session.setAttribute("access_token_user_email", reqRec.email);
        session.setAttribute("access_token_issued_at", issued_at);
        session.setAttribute("access_token_expires_at", expires_at);
        return true;
    }

}
