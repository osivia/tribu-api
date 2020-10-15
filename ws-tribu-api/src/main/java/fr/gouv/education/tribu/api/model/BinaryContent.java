package fr.gouv.education.tribu.api.model;

import java.io.File;
import java.io.InputStream;

import org.nuxeo.ecm.automation.client.Session;

/**
 * 
 *
 */
public class BinaryContent {

	private String fileName;
	private File tempFile;
	private String mimeType;
	private long count;
	private InputStream stream;
	private Session session;

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public long getCount() {
		return count;
	}
	
	

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setName(String fileName) {
		this.fileName = fileName;
		
	}

	public void setFile(File tempFile) {
		this.tempFile = tempFile;
		
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
		
	}

	public void setFileSize(long count) {
		this.count = count;
		
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
		
	}

	public void setLongLiveSession(Session session) {
		this.session = session;
		
	}

}
