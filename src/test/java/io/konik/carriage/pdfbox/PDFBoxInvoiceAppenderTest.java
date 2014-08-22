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

import static com.google.common.io.ByteStreams.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;
import io.konik.InvoiceTransformer;
import io.konik.harness.InvoiceAppender;
import io.konik.zugferd.Invoice;

import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

@SuppressWarnings("javadoc")
public class PDFBoxInvoiceAppenderTest {
   
   private static final String MUSTERRECHNUNG_EINFACH_XML = "/Musterrechnung_Einfach.xml";
   private static final String ACME_INVOICE_42_PDF = "/acme_invoice-42.pdf";
   
   private static final String TARGET_ACME_INVOICE_42_PDF = "target/acme_invoice-42.pdf";
   private static final String TARGET_ACME_INVOICE_42_RANDOM_PDF = "target/acme_invoice-42_random.pdf";
   
   InvoiceAppender appender;
   InputStream isPdf;
   InputStream isXml;
   InvoiceTransformer transformer = new InvoiceTransformer();

   @Before
   public void setUp() throws Exception {
      appender = new PDFBoxInvoiceAppender();
      isPdf = getClass().getResourceAsStream(ACME_INVOICE_42_PDF);
      isXml = getClass().getResourceAsStream(MUSTERRECHNUNG_EINFACH_XML);
   }

   @Test
   public void appendInputStream() throws Exception {
      Invoice invoice = transformer.toModel(isXml);
      byte[] pdfInput = toByteArray(isPdf);
      byte[] outPdf = appender.append(invoice, pdfInput);
      assertThat(outPdf).isNotNull();

      Files.write(outPdf , new File(TARGET_ACME_INVOICE_42_PDF));
   }

   @Test
   public void appendInputStream_random() throws Exception {
      Invoice invoice = transformer.toModel(isXml);
      byte[] pdfInput = toByteArray(isPdf);
      byte[] outPdf = appender.append(invoice, pdfInput);
      assertThat(outPdf).isNotNull();

      Files.write(outPdf , new File(TARGET_ACME_INVOICE_42_RANDOM_PDF));
   }
   
   @Test
   public void appendByteArray() throws Exception {
      Invoice invoice = transformer.toModel(isXml);
      byte[] outPdf = appender.append(invoice, toByteArray(isPdf));
      assertThat(outPdf).isNotNull();
   }
   
}
