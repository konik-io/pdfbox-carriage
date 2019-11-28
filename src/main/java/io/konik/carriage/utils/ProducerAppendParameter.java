package io.konik.carriage.utils;

import io.konik.harness.appender.DefaultAppendParameter;

import java.io.InputStream;
import java.io.OutputStream;

public class ProducerAppendParameter extends DefaultAppendParameter {

    private final String producer;
    /**
     * Instantiates a new default append parameter.
     *
     * @param inputPdf                the input pdf
     * @param attachmentFile          the attachment file
     * @param resultingPdf            the resulting pdf
     * @param zugferdVersion          the zugferd version
     * @param zugferdConformanceLevel the zugferd conformance level
     */
    public ProducerAppendParameter(InputStream inputPdf, InputStream attachmentFile, OutputStream resultingPdf, String zugferdVersion, String zugferdConformanceLevel) {
        super(inputPdf, attachmentFile, resultingPdf, zugferdVersion, zugferdConformanceLevel);
        producer = "Konik Library";
    }

    /**
     * Instantiates a new default append parameter.
     * @param inputPdf                the input pdf
     * @param attachmentFile          the attachment file
     * @param resultingPdf            the resulting pdf
     * @param zugferdVersion          the zugferd version
     * @param zugferdConformanceLevel the zugferd conformance level
     * @param producer                The produces name that is backed into the PDF
     */
    public ProducerAppendParameter(InputStream inputPdf, InputStream attachmentFile, OutputStream resultingPdf, String zugferdVersion, String zugferdConformanceLevel, String producer) {
        super(inputPdf, attachmentFile, resultingPdf, zugferdVersion, zugferdConformanceLevel);
        this.producer = producer;
    }


    public String getProducer() {
        return producer;
    }
}
