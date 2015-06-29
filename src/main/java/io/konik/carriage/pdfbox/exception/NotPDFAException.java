package io.konik.carriage.pdfbox.exception;

/**
 * Is thrown, when find out that the PDF is not of type PDF/A.
 *
 */
public class NotPDFAException extends RuntimeException {

   private static final long serialVersionUID = 6138692940701494870L;
   
   private final static String MSG = "The provided PDF is not of type PDF/A. Contact support for an additional PDF to PDF/A conversation";
   /**
    *  
    */
   public NotPDFAException() {
      super(MSG);
   }
}
