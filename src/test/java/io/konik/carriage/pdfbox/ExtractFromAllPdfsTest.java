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

import static com.google.common.io.Files.getFileExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

@SuppressWarnings("javadoc")
@RunWith(Parameterized.class)
public class ExtractFromAllPdfsTest {

   private static final String TEST_FILE_LOCATION = "src/test/resources/all";
   
   @Parameter
   public File testFile;
   
   @Parameter(value=1)
   public String testFileName;
   
   PDFBoxInvoiceExtractor extractor = new PDFBoxInvoiceExtractor();
   
   @Parameters(name = "{1}")
   public static Iterable<Object[]> findAllPdfInvoiceFiles() {
      Collection<Object[]> result = Lists.newArrayList();
      File pdfDir = new File(TEST_FILE_LOCATION);
      Iterable<File> traversal = Files.fileTreeTraverser().children(pdfDir);
      for (File file : traversal) {
         if (file.isFile() && getFileExtension(file.getName()).equals("pdf")){
            result.add(new Object[]{file,file.getName()});
         }
      }
      return result;
   }
   
   @Test
   @Ignore("not needed now")
   public void extractFromAllPdfs() throws FileNotFoundException, IOException {
      //execute
       byte[] extract = extractor.extract(new FileInputStream(testFile));
      
      
      Files.asByteSink(new File("./target/"+testFileName.replace(".pdf", ".xml"))).write(extract);

      //verify
      assertThat(extract).isNotNull();
   }
}
