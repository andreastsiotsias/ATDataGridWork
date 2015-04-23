package ATWebAuth;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GB001894
 */
import ATDataGrid.servlets.GooglePlusSignInTokenInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
 
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
 
public class HttpClientExample {
 
	private final String USER_AGENT = "Mozilla/5.0";
 
	public static void main(String[] args) throws Exception {
 
		HttpClientExample http = new HttpClientExample();
 
		System.out.println("Send Http GET request");
		boolean isValid = http.doAccessTokenValidation();
                System.out.println ("Validated : "+isValid);
 
	}
 
	// HTTP GET request
	private boolean doAccessTokenValidation () throws Exception {
            String access_token="ya29.XgHOFfMKYSSkT6oePTOP8PHuoa8vFlEm65Y3mmIJAgwhTBNL6zEQva2RFMIOl_joxzn2j_L-Jt6HbA";
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
            return true;
	}
 }
