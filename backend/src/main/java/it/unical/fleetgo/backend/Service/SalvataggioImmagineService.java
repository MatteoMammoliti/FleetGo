package it.unical.fleetgo.backend.Service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class SalvataggioImmagineService {

    @Autowired private Cloudinary cloudinary;

    public String salvaImmagine(MultipartFile immagine, String nomeCartella) throws IOException {
        return (String) cloudinary.uploader().upload(immagine.getBytes(),
                Map.of("folder", nomeCartella)).get("secure_url");
    }

    public void eliminaImmagine(String urlImmagine) throws IOException {

        if (urlImmagine == null || urlImmagine.isEmpty()) {
            throw new IllegalArgumentException();
        }

        String publicId = this.estraiPublicIdDaUrl(urlImmagine);

        if(publicId == null) {
            throw new IllegalArgumentException();
        }

        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new IOException("Errore durante l'eliminazione dell'immagine da Cloudinary");
        }
    }

    private String estraiPublicIdDaUrl(String url) {

        try {
            String splitMarker = "/upload/";
            int splitIndex = url.indexOf(splitMarker);

            if (splitIndex == -1) return null;

            String path = url.substring(splitIndex + splitMarker.length());

            if (path.startsWith("v")) {
                int indiceInizioVersione = path.indexOf("/");
                if (indiceInizioVersione != -1) {
                    path = path.substring(indiceInizioVersione + 1);
                }
            }

            int indicePuntoFineUrl = path.lastIndexOf(".");
            if (indicePuntoFineUrl != -1) {
                path = path.substring(0, indicePuntoFineUrl);
            }

            return path;
        } catch (Exception e) {
            return null;
        }
    }
}