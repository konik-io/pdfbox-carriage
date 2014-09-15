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

import io.konik.harness.AppendParameter;
import io.konik.harness.appender.DefaultAppendParameter;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
public class PDFBoxInvoiceAppenderTest {
   
   private static final String MUSTERRECHNUNG_EINFACH_XML = "/Musterrechnung_Einfach.xml";
   private static final String ACME_INVOICE_42_PDF = "/acme_invoice-42.pdf";
   
   PDFBoxInvoiceAppender appender;

   @Before
   public void setUp() throws Exception {
      appender = new PDFBoxInvoiceAppender();
   }

   @Test
   public void appendInputStream() throws Exception {
      InputStream isPdf = getClass().getResourceAsStream(ACME_INVOICE_42_PDF);
      InputStream isXml = getClass().getResourceAsStream(MUSTERRECHNUNG_EINFACH_XML);
      FileOutputStream outputPdf = new FileOutputStream("target/acme_invoice-42_zf.pdf");
      AppendParameter appendParameter = new DefaultAppendParameter(isPdf, isXml,outputPdf,"1.0","BASIC");
      appender.append(appendParameter);
   }
   
}
