/**
 * 
 */
package com.emc.it.airwatchplugin.service;

import hudson.model.BuildListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.emc.it.airwatchplugin.AirwatchDTO;
import com.google.gson.Gson;

/**
 * @author elumam	
 *
 */
public class AirwatchClient {

	
	
	public void deploy(BuildListener listener, File file, String apiUrl, String apiTenent, String appName, int chunkSize) throws IOException{
		long appsize = file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[chunkSize];
		int read = 0;
		int seq = 0;
		AirwatchDTO response = null;
		while ((read = fis.read(buffer)) != -1) {
			listener.getLogger().println("Reading the content.");
			AirwatchDTO request = new AirwatchDTO();
			if(null!=response && response.getTransactionId()!=null){
				
				request.setChunkData(new String(encode(buffer)));
				request.setChunkSequenceNumber(seq);
				request.setChunkSize(buffer.length);
				request.setTotalApplicationSize(appsize);
				request.setTransactionId(response.getTransactionId());
			}else{
				request.setChunkData(new String(encode(buffer)));
				request.setChunkSequenceNumber(seq);
				request.setChunkSize(buffer.length);
				request.setTotalApplicationSize(appsize);
			}
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(apiUrl);
			Gson gson = new Gson();
			String json = gson.toJson(request, AirwatchDTO.class);
			listener.getLogger().println("Posting "+json);
			StringEntity entity = new StringEntity(json);
			entity.setContentType("application/json");
			post.setEntity(entity);
			
			HttpResponse httpResponse = httpClient.execute(post);
			String responseString = IOUtils.toString(httpResponse.getEntity().getContent());
			listener.getLogger().println("Response "+responseString);
			response = gson.fromJson(responseString, AirwatchDTO.class);
		//	response = template.postForObject("http://localhost:8080/api/uploadchunk", request, AirwatchDTO.class);
			
			seq = seq + 1;
		}
		fis.close();
	}
	
	public static byte[] encode(byte[] bytes) {
		Encoder encoder = Base64.getEncoder();
		return encoder.encode(bytes);
	}
}
