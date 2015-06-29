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

import static org.assertj.core.api.Assertions.assertThat;
import io.konik.harness.FileExtractor;
import io.konik.harness.exception.InvoiceExtractionError;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.pdfbox.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PDFBoxInvoiceExtractorTest {

   private static final String PDFA3_FILE_NO_ATTACHMENT = "/PDFA3NoAttachment.pdf";
   private static final String ACME_INVOICE_42 = "/acme_invoice-42.pdf";
   private static final String PDF_INVOICE_LOCATION = "/Musterrechnung_Einfach.pdf";
  
   FileExtractor invoiceExtractor;

   @Before
   public void setup() {
      invoiceExtractor = new PDFBoxInvoiceExtractor();
   }

   @Test
   public void extract() throws UnsupportedEncodingException {
      //setup
      InputStream pdfStream = getClass().getResourceAsStream(PDF_INVOICE_LOCATION);

      //exec
      byte[] invoice = invoiceExtractor.extract(pdfStream);

      //very
      assertThat(invoice).isNotNull();
      assertThat(new String(invoice,"UTF-8")).containsOnlyOnce("<ram:ID>471102</ram:ID>");
   }

   @Test(expected = InvoiceExtractionError.class)
   public void extractInputStream_Fail() {
      InputStream pdfStream = getClass().getResourceAsStream(ACME_INVOICE_42);
      invoiceExtractor.extract(pdfStream);
   }

   
   @Test
   public void extract_noZfFile() {
      try {
         InputStream pdfStream = getClass().getResourceAsStream(PDFA3_FILE_NO_ATTACHMENT);
         invoiceExtractor.extract(pdfStream);
      }
      catch(InvoiceExtractionError e) {
         assertThat(e.getMessage()).startsWith(PDFBoxInvoiceExtractor.NO_ZF_FILE);
      }
   }

   @Test
   public void extractToStream() throws Exception {
    //setup
      InputStream pdfStream = getClass().getResourceAsStream(PDF_INVOICE_LOCATION);
      
      //exec
      InputStream inputStream = invoiceExtractor.extractToStream(pdfStream);
      String invoice = new String(IOUtils.toByteArray(inputStream),"UTF-8");
      
      assertThat(inputStream).isNotNull();
      assertThat(invoice).containsOnlyOnce("<ram:ID>471102</ram:ID>");
      assertThat(invoice).containsOnlyOnce("</rsm:CrossIndustryDocument>");
      inputStream.close();
   }
}
