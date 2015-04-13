/* Copyright (C) 2014 konik.io
 *
 * This file is part of the Konik library.
 *
 * The Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the Konik library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.carriage.pdfbox;

import static java.util.Collections.singletonMap;
import io.konik.carriage.pdfbox.converter.PDFAConverter;
import io.konik.carriage.pdfbox.xmp.XMPSchemaZugferd1p0;
import io.konik.carriage.utils.ByteCountingInputStream;
import io.konik.harness.AppendParameter;
import io.konik.harness.FileAppender;
import io.konik.harness.exception.InvoiceAppendError;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Scanner;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAExtensionSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.apache.xmpbox.xml.XmpSerializer;

/**
 * ZUGFeRD PDFBox Invoice Appender.
 */
@Named
@Singleton
public class PDFBoxInvoiceAppender implements FileAppender, PDFAConverter{

   private static final String PRODUCER = "Konik PDFBox-Carriage";
   private static final String MIME_TYPE = "text/xml";
   private static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";
   private final XMPMetadata zfDefaultXmp;

   /**
    * Instantiates a new PDF box invoice appender.
    */
   public PDFBoxInvoiceAppender() {
      try {
         InputStream zfExtensionIs = getClass().getResourceAsStream("/zf_extension.pdfbox.xmp");
         DomXmpParser builder = new DomXmpParser();
         builder.setStrictParsing(true);
         zfDefaultXmp = builder.parse(zfExtensionIs);
         XMPSchema schema = zfDefaultXmp.getSchema(PDFAExtensionSchema.class);
         schema.addNamespace("http://www.aiim.org/pdfa/ns/schema#", "pdfaSchema");
         schema.addNamespace("http://www.aiim.org/pdfa/ns/property#", "pdfaProperty");
      } catch (XmpParsingException e) {
         throw new InvoiceAppendError("Error initializing PDFBoxInvoiceAppender", e);
      }
   }

   @Override
   public void append(AppendParameter appendParameter) {
      InputStream inputPdf = appendParameter.inputPdf();
      try {
         PDDocument doc = PDDocument.load(inputPdf);
         checkisInputPdfA(doc);
         convertToPdfA3(doc);
         setMetadata(doc, appendParameter);
         attachZugferdFile(doc, appendParameter.attachmentFile());
         doc.getDocument().setVersion(1.7f);
         doc.save(appendParameter.resultingPdf());
         doc.close();
      } catch (Exception e) {
         throw new InvoiceAppendError("Error appending Invoice the input Strem is: " + inputPdf, e);
      }

   }

   protected void convertToPdfA3(PDDocument doc) {
    if (!isConvertToPdfa3()) return; 
      //not ye
   }
   
   private static boolean isConvertToPdfa3() {
      String convert = System.getProperty("io.konik.carriage.convertToPdfA");
      return Boolean.parseBoolean(convert);
   }

   private static void checkisInputPdfA(PDDocument doc) {
      PDMetadata metadata = doc.getDocumentCatalog().getMetadata();
      try {
         InputStream inputStream = metadata.createInputStream();
         Scanner streamScanner = new Scanner(inputStream);
         String found = streamScanner.findWithinHorizon("http://www.aiim.org/pdfa/ns/id", 0);
         streamScanner.close();
         if (found==null && !isConvertToPdfa3()  ) {
            throw new InvoiceAppendError("The provided PDF is not of type PDF/A. Contact support for a PDF to PDF/A conversation");
         }
      } catch (IOException e) {
         throw new InvoiceAppendError("Could not read PDF Metadata",e);
      }
   }

   private static void attachZugferdFile(PDDocument doc, InputStream zugferdFile) throws IOException {
      PDEmbeddedFilesNameTreeNode fileNameTreeNode = new PDEmbeddedFilesNameTreeNode();

      PDEmbeddedFile embeddedFile = createEmbeddedFile(doc, zugferdFile);
      embeddedFile.addCompression();
      PDComplexFileSpecification fileSpecification = createFileSpecification(embeddedFile);

      COSDictionary dict = fileSpecification.getCOSDictionary();
      dict.setName("AFRelationship", "Alternative");
      dict.setString("UF", ZF_FILE_NAME);

      fileNameTreeNode.setNames(singletonMap(ZF_FILE_NAME, fileSpecification));

      setNamesDictionary(doc, fileNameTreeNode);

      COSArray cosArray = new COSArray();
      cosArray.add(fileSpecification);
      doc.getDocumentCatalog().getCOSObject().setItem("AF", cosArray);
   }

   private static PDComplexFileSpecification createFileSpecification(PDEmbeddedFile embeddedFile) {
      PDComplexFileSpecification fileSpecification = new PDComplexFileSpecification();
      fileSpecification.setFile(ZF_FILE_NAME);
      fileSpecification.setEmbeddedFile(embeddedFile);
      return fileSpecification;
   }

   private static PDEmbeddedFile createEmbeddedFile(PDDocument doc, InputStream zugferdFile) throws IOException {
      Calendar now = Calendar.getInstance();
      ByteCountingInputStream countingIs = new ByteCountingInputStream(zugferdFile);
      PDEmbeddedFile embeddedFile = new PDEmbeddedFile(doc, countingIs);
      embeddedFile.addCompression();
      embeddedFile.setSubtype(MIME_TYPE);
      embeddedFile.setSize(countingIs.getByteCount());
      embeddedFile.setCreationDate(now);
      embeddedFile.setModDate(now);
      return embeddedFile;
   }

   private static void setNamesDictionary(PDDocument doc, PDEmbeddedFilesNameTreeNode fileNameTreeNode) {
      PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
      PDDocumentNameDictionary namesDictionary = new PDDocumentNameDictionary(documentCatalog);
      namesDictionary.setEmbeddedFiles(fileNameTreeNode);
      documentCatalog.setNames(namesDictionary);
   }

   private void setMetadata(PDDocument doc, AppendParameter appendParameter) throws IOException, TransformerException,
         BadFieldValueException {
      Calendar now = Calendar.getInstance();
      PDDocumentCatalog catalog = doc.getDocumentCatalog();

      PDMetadata metadata = new PDMetadata(doc);
      catalog.setMetadata(metadata);

      XMPMetadata xmp = XMPMetadata.createXMPMetadata();
      PDFAIdentificationSchema pdfaid = new PDFAIdentificationSchema(xmp);
      pdfaid.setPart(Integer.valueOf(3));
      pdfaid.setConformance("B");
      xmp.addSchema(pdfaid);

      DublinCoreSchema dublicCore = new DublinCoreSchema(xmp);
      xmp.addSchema(dublicCore);

      XMPBasicSchema basicSchema = new XMPBasicSchema(xmp);
      basicSchema.setCreatorTool(PRODUCER);
      basicSchema.setCreateDate(now);
      xmp.addSchema(basicSchema);

      PDDocumentInformation pdi = doc.getDocumentInformation();
      pdi.setModificationDate(now);
      pdi.setProducer(PRODUCER);
      pdi.setAuthor(getAuthor());
      doc.setDocumentInformation(pdi);

      AdobePDFSchema pdf = new AdobePDFSchema(xmp);
      pdf.setProducer(PRODUCER);
      xmp.addSchema(pdf);

      PDMarkInfo markinfo = new PDMarkInfo();
      markinfo.setMarked(true);
      doc.getDocumentCatalog().setMarkInfo(markinfo);

      xmp.addSchema(zfDefaultXmp.getPDFExtensionSchema());
      XMPSchemaZugferd1p0 zf = new XMPSchemaZugferd1p0(xmp);
      zf.setConformanceLevel(appendParameter.zugferdConformanceLevel());
      zf.setVersion(appendParameter.zugferdVersion());
      xmp.addSchema(zf);

      new XmpSerializer().serialize(xmp, metadata.createOutputStream(), true);
   }

   private static String getAuthor() {
      return System.getProperty("user.name");
   }

}
