/*
 * Copyright 2014 Konik.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.konik.carriage.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * Input Stream that also counting Byte Bytes.
 */
public final class ByteCountingInputStream extends FilterInputStream {
   private int byteCount;
   private int mark = -1;

   /**
    * Instantiates a new byte counting input stream.
    * @param in the in
    */
   public ByteCountingInputStream(InputStream in) {
      super(in);
   }

   /**
    * Returns bytes read.
    *
    * @return the count
    */
   public int getByteCount() {
      return byteCount;
   }

   @Override
   public int read() throws IOException {
      int result = in.read();
      if (result != -1) {
         byteCount++;
      }
      return result;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      int read = in.read(b, off, len);
      if (read != -1) {
         byteCount += read;
      }
      return read;
   }

   @Override
   public long skip(long n) throws IOException {
      long skip = in.skip(n);
      byteCount += skip;
      return skip;
   }

   @Override
   public synchronized void mark(int readlimit) {
      in.mark(readlimit);
      mark = byteCount;
   }

   @Override
   public synchronized void reset() throws IOException {
      if (!in.markSupported()) { throw new IOException("Mark is not supported"); }
      if (mark == -1) { throw new IOException("Mark not set"); }
      in.reset();
      byteCount = mark;
   }
}
