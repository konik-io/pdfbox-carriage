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

import io.konik.InvoiceTransformer;
import io.konik.harness.InvoiceExtractionError;
import io.konik.harness.InvoiceExtractor;
import io.konik.zugferd.Invoice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

/**
 * The PDFBoxInvoice Extractor.
 */
public class PDFBoxInvoiceExtractor implements InvoiceExtractor {

   static final String NO_FILE = "Provided PDF does not contain embedded files.";
   static final String NO_ZF_FILE = "PDF does not contain expected embedded file named ";
   static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";

   InvoiceTransformer invoiceTransformer = new InvoiceTransformer();

   @Override
   public Invoice extract(byte[] pdfIn) {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfIn);
      return extract(byteArrayInputStream);
   }

   @Override
   public Invoice extract(InputStream pdfStream) {
      try {
         return extractInvoiceFromPdfStream(pdfStream);
      } catch (IOException e) {
         throw new InvoiceExtractionError("Error extracting content from PDF: " + e.getMessage());
      }
   }

   private Invoice extractInvoiceFromPdfStream(InputStream pdfStream) throws IOException {
      PDDocument doc = PDDocument.load(pdfStream);
      try {
         return extractInvoiceFormPdf(doc);
      }finally {
         doc.close();
      }
   }

   private Invoice extractInvoiceFormPdf(PDDocument doc) throws IOException {
      PDDocumentNameDictionary nameDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
      PDEmbeddedFilesNameTreeNode embeddedFiles = getEmbeddedFiles(nameDictionary);
      PDEmbeddedFile embeddedFile = extractZugferdInvocieFile(embeddedFiles);
      InputStream xmlInvoiceStream = embeddedFile.createInputStream();
      Invoice invoice = invoiceTransformer.toModel(xmlInvoiceStream);
      return invoice;
   }

   private static PDEmbeddedFilesNameTreeNode getEmbeddedFiles(PDDocumentNameDictionary names) {
      PDEmbeddedFilesNameTreeNode embeddedFiles = names.getEmbeddedFiles();
      if (embeddedFiles == null) { throw new InvoiceExtractionError(NO_FILE); }
      return embeddedFiles;
   }
   
   private static PDEmbeddedFile extractZugferdInvocieFile(PDEmbeddedFilesNameTreeNode embeddedFiles)
         throws IOException {
      PDComplexFileSpecification fileSpec = (PDComplexFileSpecification) embeddedFiles.getValue(ZF_FILE_NAME);
      if (fileSpec == null) { throw new InvoiceExtractionError(NO_ZF_FILE + ZF_FILE_NAME); }
      PDEmbeddedFile embeddedFile = fileSpec.getEmbeddedFile();
      return embeddedFile;
   }
}
