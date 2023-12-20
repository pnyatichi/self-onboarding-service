package com.coopbank.selfonboarding.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class PdfConverterService {
	public static String generatePdfBase64(String htmlContent) {
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            renderer.layout();
            renderer.createPDF(outputStream, false);
            renderer.finishPDF();

            byte[] pdfBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
