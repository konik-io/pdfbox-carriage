package io.konik.regression;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class COSStreamEncodingDecoding {

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
   }

   @Before
   public void setUp() throws Exception {
   }

   @Test
   public void test() throws IOException, TransformerException {
      InputStream pdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      PDDocument pdDocument = new PDDocument().load(pdfStream);
      
      PDDocumentCatalog catalog = pdDocument.getDocumentCatalog();
      
      PDMetadata metadata = new PDMetadata(pdDocument);
      catalog.setMetadata(metadata);

      XMPMetadata xmp = XMPMetadata.createXMPMetadata();
      
      DublinCoreSchema dublicCore = new DublinCoreSchema(xmp);
      dublicCore.setTitle("SimpleTitle");
      xmp.addSchema(dublicCore);
      
      OutputStream metaOutputStream = metadata.createOutputStream();
      new XmpSerializer().serialize(xmp, metaOutputStream, true);
      metaOutputStream.close();
      
      pdDocument.save("result.pdf");
     
      pdDocument.close();
   }

}
