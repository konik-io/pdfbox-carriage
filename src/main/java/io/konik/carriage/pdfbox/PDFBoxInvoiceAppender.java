/*
 * Copyright (C) 2014 Konik.io
 *
 * This file is part of Konik library.
 *
 * Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Konik library.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.carriage.pdfbox;

import static java.util.Collections.singletonMap;
import io.konik.carriage.utils.ByteCountingInputStream;
import io.konik.harness.AppendParameter;
import io.konik.harness.FileAppender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

/**
 *  ZUGFeRD PDFBox Invoice Appender.
 */
@Named
@Singleton
public class PDFBoxInvoiceAppender implements FileAppender {

   private static final String MIME_TYPE = "text/xml";
   private static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";

   Calendar now = Calendar.getInstance();

   @Override
   public void append(AppendParameter appendParameter) {
      InputStream inputPdf = appendParameter.inputPdf();
      try {
         PDDocument doc = PDDocument.load(inputPdf);
         attachZugferdFile(doc, appendParameter.attachmentFile());
         doc.save(appendParameter.resultingPdf());
         doc.close();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (COSVisitorException e) {
         e.printStackTrace();
      }
   }

   private void attachZugferdFile(PDDocument doc, InputStream zugferdFile) throws IOException {
      PDEmbeddedFilesNameTreeNode fileNameTreeNode = new PDEmbeddedFilesNameTreeNode();

      PDEmbeddedFile embeddedFile = createEmbeddedFile(doc, zugferdFile);
      PDComplexFileSpecification fileSpecification = createFileSpecification(embeddedFile);

      COSDictionary dict = fileSpecification.getCOSDictionary();
      dict.setName("AFRelationship", "Alternative");
      dict.setString("UF", ZF_FILE_NAME);

      fileNameTreeNode.setNames(singletonMap(ZF_FILE_NAME, fileSpecification));

      setNamesDictionary(doc, fileNameTreeNode);

      COSArray cosArray = new COSArray();
      cosArray.add(fileSpecification);
      doc.getDocumentCatalog().getCOSDictionary().setItem("AF", cosArray);
   }



   private static PDComplexFileSpecification createFileSpecification(PDEmbeddedFile embeddedFile) {
      PDComplexFileSpecification fileSpecification = new PDComplexFileSpecification();
      fileSpecification.setFile(ZF_FILE_NAME);
      fileSpecification.setEmbeddedFile(embeddedFile);
      return fileSpecification;
   }

   private PDEmbeddedFile createEmbeddedFile(PDDocument doc, InputStream zugferdFile) throws IOException {
      ByteCountingInputStream countingIs = new ByteCountingInputStream(zugferdFile);
      PDEmbeddedFile embeddedFile = new PDEmbeddedFile(doc, countingIs);
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
}
