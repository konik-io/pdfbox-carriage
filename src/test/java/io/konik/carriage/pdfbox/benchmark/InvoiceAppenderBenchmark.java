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
import io.konik.harness.appender.DefaultAppendParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.RunnerException;

@SuppressWarnings("javadoc")
@State(Thread)
@BenchmarkMode(Throughput)
@OutputTimeUnit(SECONDS)
public class InvoiceAppenderBenchmark extends DefaultBenchmark {

   private final FileAppender appender = new PDFBoxInvoiceAppender();
   

   @Setup
   public void setup() {
   }

   @Benchmark
   public void append() throws Exception {
      InputStream xmlIs = getClass().getResourceAsStream("/Musterrechnung_Einfach.xml");
      InputStream pdfIn = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      appender.append(new DefaultAppendParameter(pdfIn, xmlIs, new ByteArrayOutputStream(), "1.0", "TEST"));
   }
   
   @Benchmark
   @Threads(4)
   public void append_witAndThreads() throws Exception {
      InputStream xmlIs = getClass().getResourceAsStream("/Musterrechnung_Einfach.xml");
      InputStream pdfIn = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      appender.append(new DefaultAppendParameter(pdfIn, xmlIs, new ByteArrayOutputStream(), "1.0", "TEST"));
   }
   
   @Test
   public void benchmark_iTextPdfInvoiceAppender() throws RunnerException {
      runDefault();
   }
   
}
