/* Copyright (C) 2014 konik.io
 *
 * This file is part of the Konik library.
 *
 * The Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the Konik library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.carriage.pdfbox.xmp;

import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.PDFAExtensionSchema;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.Cardinality;
import org.apache.xmpbox.type.PropertyType;
import org.apache.xmpbox.type.StructuredType;
import org.apache.xmpbox.type.TextType;
import org.apache.xmpbox.type.Types;

/**
 * @author Vadim Bauer
 *
 */
/**
 * 
 * The Class XMP ZUGFeRD Schema.
 * 
 * This is an example of the result::
 * <pre><code>
 *     <rdf:Description rdf:about="" xmlns:zf="urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#">
 *        <zf:ConformanceLevel>BASIC</zf:ConformanceLevel>
 *        <zf:DocumentFileName>ZUGFeRD-invoice.xml</zf:DocumentFileName>
 *        <zf:DocumentType>INVOICE</zf:DocumentType>
 *        <zf:Version>1.0</zf:Version>
 *     </rdf:Description>
 *</code></pre>
 */
@StructuredType(preferedPrefix = "zf", namespace = "urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#")
public class XMPSchemaZugferd1p0 extends PDFAExtensionSchema {

   /** The Constant CONFORMANCE_LEVEL. */
   @PropertyType(type = Types.Text, card = Cardinality.Simple)
   public static final String CONFORMANCE_LEVEL = "ConformanceLevel";

   /** The Constant DOCUMENT_FILE_NAME. */
   @PropertyType(type = Types.Text, card = Cardinality.Simple)
   public static final String DOCUMENT_FILE_NAME = "DocumentFileName";

   /** The Constant DOCUMENT_TYPE. */
   @PropertyType(type = Types.Text, card = Cardinality.Simple)
   public static final String DOCUMENT_TYPE = "DocumentType";

   /** The Constant VERSION. */
   @PropertyType(type = Types.Text, card = Cardinality.Simple)
   public static final String VERSION = "Version";
   
   @PropertyType(type = Types.PDFAType, card = Cardinality.Seq)
   public static final String VALUE_TYPE = "valueType";
   
   /**
    * Instantiates a new XMP schema ZUGFeRD 1.0. 
    * 
    * Set sensible default values
    *
    * @param metadata the parent XMP document that this schema will be part of.
    */
   public XMPSchemaZugferd1p0(XMPMetadata metadata) {
      super(metadata);
      setDocumentType("INVOICE");
      setDocumentFileName("ZUGFeRD-invoice.xml");
   }

   /**
    * Instantiates a new XMP schema zugferd1p0.
    *
    * @param metadata the metadata
    * @param customPrefix the user defined prefix that should differ from zf
    */
   public XMPSchemaZugferd1p0(XMPMetadata metadata, String customPrefix) {
      super(metadata, customPrefix);
   }

   /**
    * Gets the ZUGFeRD conformance level.
    *
    * Possible values are: BASIC, COMFORT, EXTENDED
    *
    * @return the ZUGFeRD conformance level
    */
   public TextType getConformanceLevelProperty() {
      return (TextType) getProperty(CONFORMANCE_LEVEL);
   }

   /**
    * Gets the ZUGFeRD conformance level.
    *
    * Possible values are: BASIC, COMFORT, EXTENDED
    *
    * @return the ZUGFeRD conformance level
    */
   public String getConformanceLevel() {
      TextType tt = getConformanceLevelProperty();
      return tt == null ? null : tt.getStringValue();
   }

   /**
    * Sets the ZUGFeRD conformance level.
    *
    * Possible values are: BASIC, COMFORT, EXTENDED
    *
    * @param conformanceLevel the ZUGFeRD conformance level
    */
   public void setConformanceLevel(String conformanceLevel) {
      addProperty(createTextType(CONFORMANCE_LEVEL, conformanceLevel));
   }

   /**
    * Gets the ZUGFeRD document file name.
    * 
    * Currently known value is ZUGFeRD-invoice.xml
    *
    * @return the document file name
    */
   public TextType getDocumentFileNameProperty() {
      return (TextType) getProperty(DOCUMENT_FILE_NAME);
   }

   /**
    * Gets the ZUGFeRD document file name.
    * 
    * Currently known value is ZUGFeRD-invoice.xml
    *
    * @return the document file name
    */
   public String getDocumentFileName() {
      TextType tt = getDocumentFileNameProperty();
      return tt == null ? null : tt.getStringValue();
   }

   /**
    * Sets the ZUGFeRD document file name.
    * 
    * Currently known value is ZUGFeRD-invoice.xml
    *
    * @param documentFileName the new document file name
    */
   public void setDocumentFileName(String documentFileName) {
      addProperty(createTextType(DOCUMENT_FILE_NAME, documentFileName));
   }

   /**
    * Gets the ZUGFeRD document type.
    * 
    * As of writing only INVOICE is supported
    *
    * @return the ZUGFeRD document type
    */
   public TextType getDocumentTypeProperty() {
      return (TextType) getProperty(DOCUMENT_TYPE);

   }

   /**
    * Gets the ZUGFeRD document type.
    * 
    * As of writing only INVOICE is supported
    *
    * @return the document type
    */
   public String getDocumentType() {
      TextType tt = getDocumentTypeProperty();
      return tt == null ? null : tt.getStringValue();
   }

   /**
    * Sets the ZUGFeRD document type.
    * 
    * As of writing only INVOICE is supported
    *
    * @param documentType the new document file name
    */
   public void setDocumentType(String documentType) {
      addProperty(createTextType(DOCUMENT_TYPE, documentType));
   }

   /**
    * Gets the version.
    *
    * @return the version
    */
   public TextType getVersionProperty() {
      return (TextType) getProperty(VERSION);
   }

   /**
    * Gets the version.
    *
    * @return the version
    */
   public String getVersion() {
      TextType tt = getVersionProperty();
      return tt == null ? null : tt.getStringValue();
   }

   /**
    * Sets the version.
    *
    * @param version the new version
    */
   public void setVersion(String version) {
      TextType textType = createTextType(VERSION, version);
      addProperty(textType);
   }

}