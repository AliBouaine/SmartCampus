package com.example.Reclamation.service.Service.Service;

import com.example.Reclamation.service.Entity.Reclamation;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.borders.SolidBorder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ReclamationPdfService {

    // 🎨 COLORS
    private static final DeviceRgb PRIMARY = new DeviceRgb(26, 43, 74);
    private static final DeviceRgb SECONDARY = new DeviceRgb(37, 99, 235);
    private static final DeviceRgb LIGHT_BG = new DeviceRgb(245, 247, 250);
    private static final DeviceRgb BORDER = new DeviceRgb(200, 210, 230);
    private static final DeviceRgb GREEN = new DeviceRgb(22, 163, 74);
    private static final DeviceRgb RED = new DeviceRgb(220, 38, 38);

    public byte[] generatePdf(Reclamation rec) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // 🟦 HEADER
        buildHeader(doc, bold);

        // 📦 CONTENT
        addCard(doc, "ID", String.valueOf(rec.getId()), bold, normal);
        addCard(doc, "Titre", rec.getTitre(), bold, normal);
        addCard(doc, "Description", rec.getDescription(), bold, normal);
        addCard(doc, "Email", rec.getUserEmail(), bold, normal);
        addCard(doc, "Username", rec.getUsername(), bold, normal);

        // 🟢 STATUS
        doc.add(statusBadge(rec.getStatut(), bold));

        doc.close();
        return out.toByteArray();
    }

    // 🟦 HEADER
    private void buildHeader(Document doc, PdfFont bold) {

        Paragraph title = new Paragraph("SYSTÈME DE RÉCLAMATIONS")
                .setFont(bold)
                .setFontSize(20)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);

        Div header = new Div()
                .setBackgroundColor(PRIMARY)
                .setPadding(15)
                .add(title);

        doc.add(header);
        doc.add(new Paragraph("\n"));
    }

    // 📦 CARD DESIGN
    private void addCard(Document doc, String label, String value,
                         PdfFont bold, PdfFont normal) {

        Div card = new Div()
                .setBackgroundColor(LIGHT_BG)
                .setBorder(new SolidBorder(BORDER, 1))
                .setPadding(10)
                .setMarginBottom(8);

        card.add(new Paragraph(label)
                .setFont(bold)
                .setFontSize(9)
                .setFontColor(PRIMARY));

        card.add(new Paragraph(value != null ? value : "—")
                .setFont(normal)
                .setFontSize(10));

        doc.add(card);
    }

    // 🟢 STATUS BADGE
    private Paragraph statusBadge(String status, PdfFont bold) {

        DeviceRgb color = switch (status == null ? "EN_ATTENTE" : status) {
            case "RESOLUE" -> GREEN;
            case "REJETEE" -> RED;
            default -> SECONDARY;
        };

        return new Paragraph(" " + status + " ")
                .setFont(bold)
                .setFontSize(10)
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(color)
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER);
    }
}