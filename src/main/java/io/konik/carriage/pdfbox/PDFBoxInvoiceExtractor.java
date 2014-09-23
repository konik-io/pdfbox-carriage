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

import io.konik.carriage.utils.CallBackInputStream;
import io.konik.harness.FileExtractor;
import io.konik.harness.exception.InvoiceExtractionError;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;

/**
 * The PDFBoxInvoice Extractor.
 */
public class PDFBoxInvoiceExtractor implements FileExtractor {

   static final String NO_FILE = "Provided PDF does not contain embedded files.";
   static final String NO_ZF_FILE = "The PDF does not contain an attached file named ZUGFeRD-invoice.xml. Error in: ";
   static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";

   @Override
   public byte[] extract(InputStream pdfInput) {
      InputStream attachmentFile = null;
      try {
         attachmentFile = extractToStream(pdfInput);
         return IOUtils.toByteArray(attachmentFile);
      } catch (IOException e) {
         throw new InvoiceExtractionError("Error extracting content from PDF",e);
      }finally {
         IOUtils.closeQuietly(attachmentFile);
      }
   }
   
   @Override
   public InputStream extractToStream(InputStream pdfInput) {
      try {
         return extractIntern(pdfInput);
      } catch (IOException e) {
         throw new InvoiceExtractionError("Error extracting content from PDF",e);
      }
   }
   
   private static final InputStream extractIntern(InputStream pdfStream) throws IOException {
      PDDocument doc = PDDocument.load(pdfStream);
      InputStream inputStream = extractZugferdFileAttachment(doc);
      return new CallBackInputStream(inputStream, doc);
   }
   
   private static final InputStream extractZugferdFileAttachment(PDDocument doc) throws IOException {
      PDDocumentNameDictionary nameDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
      PDEmbeddedFilesNameTreeNode embeddedFiles = listEmbeddedFiles(nameDictionary);
      return extractZugferdXmlAttachment(embeddedFiles);
   }

   private static final PDEmbeddedFilesNameTreeNode listEmbeddedFiles(PDDocumentNameDictionary names) {
      PDEmbeddedFilesNameTreeNode embeddedFiles = names.getEmbeddedFiles();
      if (embeddedFiles == null) { throw new InvoiceExtractionError(NO_FILE); }
      return embeddedFiles;
   }
   
   private static final InputStream extractZugferdXmlAttachment(PDEmbeddedFilesNameTreeNode embeddedFiles)
         throws IOException {
      PDComplexFileSpecification fileSpec = (PDComplexFileSpecification) embeddedFiles.getValue(ZF_FILE_NAME);
      if (fileSpec == null) { throw new InvoiceExtractionError(NO_ZF_FILE + ZF_FILE_NAME); }
      return fileSpec.getEmbeddedFile().createInputStream();
   }


}
