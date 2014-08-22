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


import io.konik.harness.InvoiceAppender;
import io.konik.zugferd.Invoice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Named;
import javax.inject.Singleton;


/**
 * The Class PDFBox PDF Invoice Appender.
 */
@Named
@Singleton
public class PDFBoxInvoiceAppender implements InvoiceAppender {

   private static final String INVOICE = "INVOICE";

   private static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";

   @Override
   public byte[] append(final Invoice invoice, final byte[] pdf) {
      ByteArrayInputStream isPdf = new ByteArrayInputStream(pdf);
      ByteArrayOutputStream osPdf = new ByteArrayOutputStream(pdf.length);

      append(invoice, isPdf, osPdf);

      return osPdf.toByteArray();
   }

  
   @Override
   public void append(final Invoice invoice, InputStream inputPdf, OutputStream resultingPdf) {
   }


   @Override
   public byte[] append(Invoice invoice, InputStream inputStreamPdf) {
      return null;
   }
}
