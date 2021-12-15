/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package fr.gouv.education.tribu.api.repo.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.Session;
//import org.nuxeo.ecm.automation.client.jaxrs.spi.StreamedSession;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.gouv.education.tribu.api.model.BinaryContent;
import fr.gouv.education.tribu.api.repo.NuxeoCommand;
import fr.gouv.education.tribu.api.repo.RepositoryException;

/**
 * File content command.
 *
 * @see INuxeoCommand
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileContentCommand implements NuxeoCommand {

    /** PDF content flag (used in preview). */
    public static final String PDF_CONTENT = "pdf:content";
    /** Error on PDF conversion (used in preview). */
    public static final String PDF_CONVERSION_ERROR = "errorOnPdfConversion";

    Document document;
    String docPath;
    String fieldName;

    String timestamp;
    

    
    public FileContentCommand(String docUuid) {
      this.docPath = docUuid;
      this.fieldName = "file:content";
    }


    /**
     * {@inheritDoc}
     * @throws RepositoryException 
     */
    @Override
    public Object execute(Session session) throws RepositoryException  {
    	

        try {
    	
        if (this.document == null) {
            this.document = (Document) session.newRequest("Document.Fetch").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", this.docPath).execute();
        }

        String tokens[] = this.fieldName.split("/");
        
        boolean pdfConversion = false;
        
        if(PDF_CONTENT.equals(tokens[0])){
            tokens[0] = "file:content";
            pdfConversion = true;
        }
        
        PropertyMap map = this.document.getProperties().getMap(tokens[0]);
        
        for(int i=1; i<tokens.length; i++){
            map = map.getMap(tokens[i]);
        }

        String pathFile = map.getString("data");

        FileBlob blob;
        
	        if(pdfConversion) {
	
					blob = (FileBlob) session.newRequest("Blob.AnyToPDF").setInput(this.document).execute();
	
	        } else {
	             blob = (FileBlob) session.getFile(pathFile);
	        }


        InputStream in = new FileInputStream(blob.getFile());

        File tempFile = File.createTempFile("tempFile", ".tmp");
        tempFile.deleteOnExit();

        //CountingOutputStream cout = new CountingOutputStream(new FileOutputStream(tempFile));


//        try {
//            byte[] b = new byte[1000000];
//            int i = -1;
//            while ((i = in.read(b)) != -1) {
//                cout.write(b, 0, i);
//            }
//            cout.flush();
//        } finally {
//            IOUtils.closeQuietly(in);
//            IOUtils.closeQuietly(cout);
//        }

        blob.getFile().delete();

        BinaryContent content = new BinaryContent();


        String fileName = blob.getFileName();
        if ((fileName == null) || "null".equals(fileName)) {

            fileName = this.document.getTitle();
        }

        content.setName(fileName);
        content.setFile(tempFile);
        content.setMimeType(blob.getMimeType());
        content.setFileSize(document.getLong("common:size"));
        
        

        return content;

        
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
    };


    @Override
    public String getId() {
        String id = "FileContentCommand";
        if (this.document == null) {
            id += this.docPath;
        } else {
            id += this.document;
        }

        if (this.timestamp != null) {
            id += this.timestamp;
        }

        return id += "/" + this.fieldName;
    };


}
