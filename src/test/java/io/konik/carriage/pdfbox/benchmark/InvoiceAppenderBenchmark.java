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
package io.konik.carriage.pdfbox.benchmark;

import static com.google.common.io.ByteStreams.toByteArray;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.Mode.Throughput;
import static org.openjdk.jmh.annotations.Scope.Thread;
import io.konik.carriage.pdfbox.PDFBoxInvoiceAppender;
import io.konik.harness.FileAppender;

import java.io.IOException;

import org.junit.Test;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

@SuppressWarnings("javadoc")
@State(Thread)
@BenchmarkMode(Throughput)
@OutputTimeUnit(SECONDS)
public class InvoiceAppenderBenchmark extends DefaultBenchmark {

  
//   private final InvoiceTransformer transformer = new InvoiceTransformer();
   private final FileAppender appender = new PDFBoxInvoiceAppender();
   private byte[] pdf;
   

   @Setup
   public void setup() throws IOException {
     pdf = toByteArray(getClass().getResourceAsStream("/acme_invoice-42.pdf"));
   }

//   @Benchmark
//   public void append_witStreams() throws Exception {
//      Invoice invoice = transformer.toModel(getClass().getResourceAsStream("/ZUGFeRD-invoice.xml"));
//      appender.append(invoice, getClass().getResourceAsStream("/acme_invoice-42.pdf"), new ByteArrayOutputStream());
//   }
//   
//   @Benchmark
//   @Threads(4)
//   public void append_witStreamsAndThreads() throws Exception {
//      Invoice invoice = transformer.toModel(getClass().getResourceAsStream("/ZUGFeRD-invoice.xml"));
//      appender.append(invoice, getClass().getResourceAsStream("/acme_invoice-42.pdf"), new ByteArrayOutputStream());
//   }
//   
//   @Benchmark
//   public void append_withByteArray() throws Exception {
//      Invoice invoice = transformer.toModel(getClass().getResourceAsStream("/ZUGFeRD-invoice.xml"));
//      appender.append(invoice, pdf);
//   }
   
   @Test
   public void benchmark_iTextPdfInvoiceAppender() throws RunnerException {
      runDefault();
   }
   
}
