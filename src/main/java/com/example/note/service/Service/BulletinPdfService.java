package com.example.note.service.Service;

import com.example.note.service.Entity.Note;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class BulletinPdfService {

    private static final BaseColor BLEU_HEADER = new BaseColor(23, 85, 150);
    private static final BaseColor BLEU_CLAIR = new BaseColor(189, 215, 238);
    private static final BaseColor GRIS_LIGNE = new BaseColor(242, 242, 242);
    private static final BaseColor ROUGE = new BaseColor(192, 0, 0);
    private static final BaseColor VERT = new BaseColor(0, 128, 0);

    private static final Font FONT_TITRE = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE);
    private static final Font FONT_SOUS = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.WHITE);
    private static final Font FONT_SECTION = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BLEU_HEADER);
    private static final Font FONT_TH = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font FONT_TD = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font FONT_MOY = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, BaseColor.BLACK);
    private static final Font FONT_FOOTER = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);

    public byte[] genererBulletin(List<Note> notes, Long etudiantId) throws DocumentException {

        String nomComplet = notes.isEmpty() ? "Inconnu"
                : notes.get(0).getPrenomEtudiant() + " " + notes.get(0).getNomEtudiant();
        String semestre = notes.isEmpty() ? "-" : notes.get(0).getSemestre();

        double totalPondere = 0;
        double totalCoeff = 0;
        for (Note n : notes) {
            totalPondere += n.getNote() * n.getCoefficient();
            totalCoeff += n.getCoefficient();
        }
        double moyenneGenerale = totalCoeff > 0 ? totalPondere / totalCoeff : 0;

        Document document = new Document(PageSize.A4, 40, 40, 40, 60);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);

        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter w, Document d) {
                PdfContentByte cb = w.getDirectContent();
                Phrase footer = new Phrase(
                        "Page " + w.getPageNumber() + "  •  Document généré automatiquement",
                        FONT_FOOTER
                );
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                        (d.right() - d.left()) / 2 + d.leftMargin(),
                        d.bottom() - 15, 0);
            }
        });

        document.open();

        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(BLEU_HEADER);
        headerCell.setPadding(18);
        headerCell.setBorder(Rectangle.NO_BORDER);

        Paragraph titre = new Paragraph("BULLETIN DE NOTES", FONT_TITRE);
        titre.setAlignment(Element.ALIGN_CENTER);
        Paragraph sousTitre = new Paragraph("Établissement Universitaire  •  Année 2025-2026", FONT_SOUS);
        sousTitre.setAlignment(Element.ALIGN_CENTER);

        headerCell.addElement(titre);
        headerCell.addElement(sousTitre);
        header.addCell(headerCell);
        document.add(header);
        document.add(Chunk.NEWLINE);

        document.add(sectionTitle("👤 Informations Étudiant"));
        PdfPTable infoTable = new PdfPTable(new float[]{1, 2, 1, 2});
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(15);

        ajouterInfoCell(infoTable, "Nom complet", nomComplet, false);
        ajouterInfoCell(infoTable, "ID Étudiant", etudiantId.toString(), false);
        ajouterInfoCell(infoTable, "Semestre", semestre, false);
        ajouterInfoCell(infoTable, "Nb. matières", String.valueOf(notes.size()), false);
        document.add(infoTable);

        document.add(sectionTitle("📋 Détail des Notes"));
        PdfPTable table = new PdfPTable(new float[]{3, 1.5f, 1.5f, 2});
        table.setWidthPercentage(100);
        table.setSpacingAfter(20);

        String[] headers = {"Matière", "Note /20", "Coefficient", "Note Pondérée"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, FONT_TH));
            cell.setBackgroundColor(BLEU_HEADER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            cell.setBorderColor(BaseColor.WHITE);
            table.addCell(cell);
        }

        boolean alterne = false;
        for (Note n : notes) {
            BaseColor bg = alterne ? BLEU_CLAIR : GRIS_LIGNE;
            alterne = !alterne;

            ajouterCellule(table, n.getMatiere(), bg, Element.ALIGN_LEFT);
            ajouterCelluleNote(table, n.getNote(), bg);
            ajouterCellule(table, String.valueOf(n.getCoefficient()), bg, Element.ALIGN_CENTER);
            ajouterCellule(table, String.format("%.2f", n.getNote() * n.getCoefficient()), bg, Element.ALIGN_CENTER);
        }
        document.add(table);

        document.add(sectionTitle("🎯 Résultat Final"));
        PdfPTable resultTable = new PdfPTable(new float[]{2, 1, 2});
        resultTable.setWidthPercentage(60);
        resultTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        resultTable.setSpacingAfter(20);

        PdfPCell moyLabel = new PdfPCell(new Phrase("Moyenne Générale", FONT_SECTION));
        moyLabel.setPadding(10);
        moyLabel.setBorderColor(BLEU_HEADER);
        resultTable.addCell(moyLabel);

        BaseColor couleurMoy = moyenneGenerale >= 10 ? VERT : ROUGE;
        Font fontMoy = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, couleurMoy);
        PdfPCell moyVal = new PdfPCell(new Phrase(String.format("%.2f / 20", moyenneGenerale), fontMoy));
        moyVal.setPadding(10);
        moyVal.setHorizontalAlignment(Element.ALIGN_CENTER);
        moyVal.setBorderColor(BLEU_HEADER);
        resultTable.addCell(moyVal);

        PdfPCell mentionCell = new PdfPCell(new Phrase(getMention(moyenneGenerale), FONT_MOY));
        mentionCell.setPadding(10);
        mentionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        mentionCell.setBackgroundColor(moyenneGenerale >= 10 ? VERT : ROUGE);
        mentionCell.setBorderColor(BaseColor.WHITE);
        Font fontMention = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        mentionCell.setPhrase(new Phrase(getMention(moyenneGenerale), fontMention));
        resultTable.addCell(mentionCell);

        document.add(resultTable);

        PdfPTable signTable = new PdfPTable(new float[]{1, 1});
        signTable.setWidthPercentage(100);
        signTable.setSpacingBefore(30);

        PdfPCell signProf = new PdfPCell();
        signProf.setBorder(Rectangle.TOP);
        signProf.setPaddingTop(8);
        signProf.addElement(new Paragraph("Signature du Responsable Pédagogique",
                new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY)));
        signTable.addCell(signProf);

        PdfPCell signEtud = new PdfPCell();
        signEtud.setBorder(Rectangle.TOP);
        signEtud.setPaddingTop(8);
        signEtud.addElement(new Paragraph("Signature de l'Étudiant",
                new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY)));
        signEtud.setHorizontalAlignment(Element.ALIGN_RIGHT);
        signTable.addCell(signEtud);

        document.add(signTable);

        document.close();
        return out.toByteArray();
    }

    private Paragraph sectionTitle(String text) {
        Paragraph p = new Paragraph(text, FONT_SECTION);
        p.setSpacingBefore(10);
        p.setSpacingAfter(6);
        return p;
    }

    private void ajouterInfoCell(PdfPTable t, String label, String value, boolean highlight) {
        Font fLabel = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.GRAY);
        Font fValue = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);

        PdfPCell lCell = new PdfPCell(new Phrase(label, fLabel));
        lCell.setBorder(Rectangle.NO_BORDER);
        lCell.setPadding(4);
        t.addCell(lCell);

        PdfPCell vCell = new PdfPCell(new Phrase(value, fValue));
        vCell.setBorder(Rectangle.BOTTOM);
        vCell.setBorderColor(BLEU_CLAIR);
        vCell.setPadding(4);
        t.addCell(vCell);
    }

    private void ajouterCellule(PdfPTable t, String text, BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TD));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setPadding(7);
        cell.setBorderColor(BaseColor.WHITE);
        t.addCell(cell);
    }

    private void ajouterCelluleNote(PdfPTable t, double note, BaseColor bg) {
        BaseColor color = note >= 10 ? VERT : ROUGE;
        Font f = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, color);
        PdfPCell cell = new PdfPCell(new Phrase(String.format("%.2f", note), f));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(7);
        cell.setBorderColor(BaseColor.WHITE);
        t.addCell(cell);
    }

    private String getMention(double moyenne) {
        if (moyenne >= 16) return "TRÈS BIEN";
        if (moyenne >= 14) return "BIEN";
        if (moyenne >= 12) return "ASSEZ BIEN";
        if (moyenne >= 10) return "PASSABLE";
        return "AJOURNÉ";
    }
}
