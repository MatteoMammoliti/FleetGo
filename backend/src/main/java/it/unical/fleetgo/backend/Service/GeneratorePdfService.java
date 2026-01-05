package it.unical.fleetgo.backend.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.DecimalFormat;

@Service
public class GeneratorePdfService {

    private static final Color COLORE = new Color(30, 58, 138);
    private static final String URL_LOGO_FLEET_GO = "https://res.cloudinary.com/dc6dmi9jr/image/upload/v1767554694/logoFleetGo_xbem9a.png";

    public byte[] generaPdfFattura(FatturaDTO fattura) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, bytes);

            document.open();

            PdfPTable headerFattura = new PdfPTable(2);
            headerFattura.setWidthPercentage(100);
            headerFattura.setWidths(new float[]{1, 1});

            PdfPCell cellaConLogo = new PdfPCell();
            cellaConLogo.setBorder(Rectangle.NO_BORDER);

            try {
                Image logo = Image.getInstance(new URL(URL_LOGO_FLEET_GO));

                logo.scaleToFit(150, 80);
                logo.setAlignment(Element.ALIGN_LEFT);

                cellaConLogo.addElement(logo);

                cellaConLogo.addElement(new Paragraph("\n"));

            } catch (Exception ignored) {}

            cellaConLogo.addElement(new Paragraph("FleetGo S.p.A.", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Font.BOLD, COLORE)));
            cellaConLogo.addElement(new Paragraph("Via Pietro Bucci", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            cellaConLogo.addElement(new Paragraph("87036 Rende (CS) - Italia", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            cellaConLogo.addElement(new Paragraph("P.IVA: IT12345678901", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            cellaConLogo.addElement(new Paragraph("Email: admin@fleetgo.it", FontFactory.getFont(FontFactory.HELVETICA, 10)));

            headerFattura.addCell(cellaConLogo);

            PdfPCell cellDatiFattura = new PdfPCell();
            cellDatiFattura.setBorder(Rectangle.NO_BORDER);
            cellDatiFattura.setHorizontalAlignment(Element.ALIGN_RIGHT);

            Paragraph titoloFattura = new Paragraph("FATTURA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD, Color.GRAY));
            titoloFattura.setAlignment(Element.ALIGN_RIGHT);
            cellDatiFattura.addElement(titoloFattura);

            Paragraph numeroFattura = new Paragraph("N° " + fattura.getNumeroFattura(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            numeroFattura.setAlignment(Element.ALIGN_RIGHT);
            cellDatiFattura.addElement(numeroFattura);

            String meseAnno = String.format("%02d/%d", fattura.getMeseFattura(), fattura.getAnnoFattura());
            Paragraph dataFattura = new Paragraph("Data: " + meseAnno, FontFactory.getFont(FontFactory.HELVETICA, 12));
            dataFattura.setAlignment(Element.ALIGN_RIGHT);
            cellDatiFattura.addElement(dataFattura);

            headerFattura.addCell(cellDatiFattura);
            document.add(headerFattura);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            PdfPTable tabellaAzienda = new PdfPTable(1);
            tabellaAzienda.setWidthPercentage(100);

            PdfPCell cellTitoloCliente = new PdfPCell(new Phrase("Fattura emessa a:", FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY)));
            cellTitoloCliente.setBorder(Rectangle.NO_BORDER);
            tabellaAzienda.addCell(cellTitoloCliente);

            PdfPCell nomeAzienda = new PdfPCell(new Phrase(fattura.getAzienda().getNomeAzienda(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            nomeAzienda.setBorder(Rectangle.NO_BORDER);
            tabellaAzienda.addCell(nomeAzienda);

            PdfPCell sedeAzienda = new PdfPCell(new Phrase(String.valueOf(fattura.getAzienda().getSedeAzienda()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            sedeAzienda.setBorder(Rectangle.NO_BORDER);
            tabellaAzienda.addCell(sedeAzienda);

            PdfPCell cellPivaAzienda = new PdfPCell(new Phrase("P.IVA: " + fattura.getAzienda().getPIva(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            cellPivaAzienda.setBorder(Rectangle.NO_BORDER);
            tabellaAzienda.addCell(cellPivaAzienda);

            document.add(tabellaAzienda);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            PdfPTable dettagliFattura = new PdfPTable(3);
            dettagliFattura.setWidthPercentage(100);
            dettagliFattura.setWidths(new float[]{4, 2, 1});

            aggiungiCella(dettagliFattura, "Spesa Mensile Gestione Flotta", Element.ALIGN_LEFT);
            aggiungiCella(dettagliFattura, meseAnno, Element.ALIGN_CENTER);
            aggiungiCella(dettagliFattura, formattaEuro(Double.valueOf(fattura.getCosto())), Element.ALIGN_RIGHT);

            if (fattura.getOffertaApplicata() != null) {
                aggiungiCella(dettagliFattura, "Offerta applicata: " + fattura.getOffertaApplicata().getNomeOfferta() +
                        " (Sconto " + fattura.getOffertaApplicata().getPercentualeSconto() + "%)", Element.ALIGN_LEFT);
                aggiungiCella(dettagliFattura, "", Element.ALIGN_CENTER);
                aggiungiCella(dettagliFattura, "Incluso", Element.ALIGN_RIGHT);
            }

            document.add(dettagliFattura);

            PdfPTable importi = new PdfPTable(2);
            importi.setWidthPercentage(40);
            importi.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell totaleDaPagre = new PdfPCell(new Phrase("TOTALE DA PAGARE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            totaleDaPagre.setPadding(10);
            totaleDaPagre.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT);
            importi.addCell(totaleDaPagre);

            PdfPCell cellTotaleValue = new PdfPCell(new Phrase(formattaEuro(Double.valueOf(fattura.getCosto())), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLORE)));
            cellTotaleValue.setPadding(10);
            cellTotaleValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellTotaleValue.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.RIGHT);
            importi.addCell(cellTotaleValue);

            document.add(new Paragraph("\n"));
            document.add(importi);

            document.add(new Paragraph("\n\n\n"));
            Paragraph footer = new Paragraph("Grazie per aver scelto FleetGo per la gestione della tua mobilità aziendale.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del pdf della fattura", e);
        }

        return bytes.toByteArray();
    }

    private void aggiungiCella(PdfPTable tabella, String testo, int allinneamento) {
        PdfPCell cell = new PdfPCell(new Phrase(testo, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        cell.setPadding(8);
        cell.setHorizontalAlignment(allinneamento);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setBorderColor(Color.LIGHT_GRAY);
        tabella.addCell(cell);
    }

    private String formattaEuro(Double importo) {
        if (importo == null) return "€ 0,00";
        DecimalFormat df = new DecimalFormat("€ #,##0.00");
        return df.format(importo);
    }
}