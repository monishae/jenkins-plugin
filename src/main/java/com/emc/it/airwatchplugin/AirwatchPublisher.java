package com.emc.it.airwatchplugin;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import com.emc.it.airwatchplugin.service.AirwatchClient;

/**
 * Airwatch artifact publisher. 
 * @author elumam
 *
 */
@SuppressWarnings("unchecked")
public class AirwatchPublisher extends Recorder {

	private final String appName;
	private final String airwatchUrl;
	private final String apiTenentCode;
	private final String filePattern;
	private final int chunkSize;
	//private static final int chunkSize = 1024;
	
	@DataBoundConstructor	
	public AirwatchPublisher(String appName, String airwatchUrl,
			String apiTenentCode,String filePattern,int chunkSize) {
		super();
		this.appName = appName;
		this.airwatchUrl = airwatchUrl;
		this.apiTenentCode = apiTenentCode;
		this.filePattern=filePattern;
		this.chunkSize=chunkSize;
	}


	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}


	public String getAppName() {
		return appName;
	}


	public String getAirwatchUrl() {
		return airwatchUrl;
	}


	public String getApiTenentCode() {
		return apiTenentCode;
	}
		
   
	public String getFilePattern() {
		return filePattern;
	}


	public int getChunksize() {
		return chunkSize;
	}


	@Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }
    
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        listener.getLogger().println("Deploying to Airwatch..."); //$NON-NLS-1$
        listener.getLogger().println("airwatchUrl "+airwatchUrl);
        listener.getLogger().println("apiTenentCode "+apiTenentCode);
        listener.getLogger().println("appName "+appName);
        listener.getLogger().println("filePattern "+filePattern);
        listener.getLogger().println("chunkSize "+chunkSize);
        
        FileFinder fileFinder = new FileFinder(filePattern);
        FilePath rootDir;
        
        
		rootDir = build.getWorkspace();
		if (rootDir == null) { // slave down?
			listener.error("Build workspace unavailable.");
			return false;
		}
	
        
        listener.getLogger().println("Root directory to find files to upload : " + rootDir);

		List<String> fileNames = rootDir.act(fileFinder);
		 listener.getLogger().println("found remote files : " + fileNames.size());
		 if (fileNames.isEmpty()) {
			 listener.error("Could not find the file specified by the pattern " + filePattern);
			return false;
		}
		boolean result = true;
		AirwatchClient airwatchClient = new AirwatchClient();
		for(String filename: fileNames){
			listener.getLogger().println("found remote files : " + filename);
			File tempFile = File.createTempFile("jenkins", "temp-airwatch-deploy."+FilenameUtils.getExtension(filename));
			FileWriter fw = new FileWriter(tempFile);
			FileReader fr = new FileReader(new File(rootDir+"\\"+filename));
			int i = 0;
			char[] buff = new char[chunkSize];
			
			while((i=fr.read(buff))!=-1){
				fw.write(buff);
			}
			fw.close();
			fr.close();
			listener.getLogger().println("Size : " + tempFile.length());
			airwatchClient.deploy(listener, tempFile, airwatchUrl, apiTenentCode, appName, chunkSize);
		}
		return result;

    }
    

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Publish to Airwatch";
		}
		  
	}
	
	
	public String getStringOfInputStream(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}

}


