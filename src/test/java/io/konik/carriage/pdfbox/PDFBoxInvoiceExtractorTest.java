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
import io.konik.carriage.pdfbox.PDFBoxInvoiceExtractor;
import io.konik.harness.InvoiceExtractionError;
import io.konik.zugferd.Invoice;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PDFBoxInvoiceExtractorTest {

   private static final String PDF_INVOICE_LOCATION = "/Musterrechnung_Einfach.pdf";
   PDFBoxInvoiceExtractor invoiceExtractor;

   @Before
   public void setup() {
      invoiceExtractor = new PDFBoxInvoiceExtractor();
   }

   @Test
   public void extractByteArray() throws Exception {
      //setup
      URL resource = getClass().getResource(PDF_INVOICE_LOCATION);
      byte[] pdf = new byte[resource.openConnection().getContentLength()];
      DataInputStream dataIs = new DataInputStream(resource.openStream());
      dataIs.readFully(pdf);

      //exec
      Invoice invoice = invoiceExtractor.extract(pdf);

      //validate
      assertThat(invoice).isNotNull();
      assertThat(invoice.getHeader().getInvoiceNumber()).isEqualTo("471102");
   }

   @Test
   public void extractInputStream() {
      //setup
      InputStream pdfStream = getClass().getResourceAsStream(PDF_INVOICE_LOCATION);

      //exec
      Invoice invoice = invoiceExtractor.extract(pdfStream);

      //very
      assertThat(invoice).isNotNull();
      assertThat(invoice.getHeader().getInvoiceNumber()).isEqualTo("471102");
   }

   @Test(expected = InvoiceExtractionError.class)
   public void extractInputStream_Fail() {
      InputStream pdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      invoiceExtractor.extract(pdfStream);
   }

   
   @Test
   public void extractInputStream_noZfFile() {
      try {
         InputStream pdfStream = getClass().getResourceAsStream("/PDFA3FileAttachment.pdf");
         invoiceExtractor.extract(pdfStream);
      }
      catch(InvoiceExtractionError e) {
         assertThat(e.getMessage()).startsWith(PDFBoxInvoiceExtractor.NO_ZF_FILE);
      }
   }
}
