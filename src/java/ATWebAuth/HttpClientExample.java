/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GB001894
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
 
public class HttpClientExample {
 
	private final String USER_AGENT = "Mozilla/5.0";
 
	public static void main(String[] args) throws Exception {
 
		HttpClientExample http = new HttpClientExample();
 
		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();
 
		//System.out.println("\nTesting 2 - Send Http POST request");
		//http.sendPost();
 
	}
 
	// HTTP GET request
	private void sendGet() throws Exception {
 
		//String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=ya29.WAH0guH9V_-7j4TXc6Jnxso5HQbxLH2FdeXdbW62_xN9P1UDHhFjI41EFmkudoFjN2K04LH4klFRFw";
                //String url = "https://www.googleapis.com/userinfo/v2/me?access_token=ya29.WAG6HgyHWWtotn70kzjLdmj_yAMVRk-HE3DvGlEhJUeUCoFr3cuZgmqC1qQoRRiiJWRFm6dQn9HIFQ";
                String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=ya29.WAEy-2TQ_mmR0jGUsQU0eExPB597HsyIQvs2GJQDzDelsz0sz-6ogXb-WcGqMvA6sf6LNVNnT93jsg";
 
		//HttpClient client = new DefaultHttpClient();
                HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
 
		// add request header
		request.addHeader("User-Agent", USER_AGENT);
 
                System.out.println("\nSending 'GET' request to URL : " + url);
		HttpResponse response = client.execute(request);
 
		System.out.println("Response Code : " + 
                       response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
 
	}
 
	// HTTP POST request
	private void sendPost() throws Exception {
 
		String url = "https://selfsolve.apple.com/wcResults.do";
 
		//HttpClient client = new DefaultHttpClient();
		HttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(url);
 
		// add header
		post.setHeader("User-Agent", USER_AGENT);
 
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
		urlParameters.add(new BasicNameValuePair("cn", ""));
		urlParameters.add(new BasicNameValuePair("locale", ""));
		urlParameters.add(new BasicNameValuePair("caller", ""));
		urlParameters.add(new BasicNameValuePair("num", "12345"));
 
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
 
		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
 
	}
 
}
