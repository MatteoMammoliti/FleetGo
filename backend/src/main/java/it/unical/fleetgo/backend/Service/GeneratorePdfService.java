package it.unical.fleetgo.backend.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import it.unical.fleetgo.backend.Models.DTO.FatturaDTO;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GeneratorePdfService {

    public byte[] generaPdfFattura(FatturaDTO fattura) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, bytes);

            document.open();

            Paragraph titolo = new Paragraph(
                    "Fattura FleetGo",
                    new Font(FontFactory.getFont(
                            FontFactory.HELVETICA, 20, Font.BOLD)
                    )
            );

            document.add(titolo);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Dettagli fattura:",
                    new Font(FontFactory.getFont(
                            FontFactory.HELVETICA, 16, Font.BOLD)
                    )
            ));

            document.add(new Paragraph("Numero Fattura: " + fattura.getNumeroFattura()));
            document.add(new Paragraph("Data Fattura: " + fattura.getMeseFattura() + "/" + fattura.getAnnoFattura()));
            document.add(new Paragraph("Azienda: " + fattura.getAzienda().getNomeAzienda()));
            document.add(new Paragraph("Sede Azienda: " + fattura.getAzienda().getSedeAzienda()));
            document.add(new Paragraph("PIva Azienda: " + fattura.getAzienda().getPIva()));

            document.add(new Paragraph("\n"));

            Font fontImporto = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph importo = new Paragraph("Totale da pagare: € " + fattura.getCosto(), fontImporto);
            importo.setAlignment(Element.ALIGN_RIGHT);
            document.add(importo);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del pdf della fattura", e);
        }

        return bytes.toByteArray();
    }
}