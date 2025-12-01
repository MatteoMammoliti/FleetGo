package it.unical.fleetgo.backend.Service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class SalvataggioImmagineVeicolo {
    @Autowired
    private Cloudinary cloudinary;


    public String salvaImmagine(MultipartFile immagine) throws IOException {
        Map result = cloudinary.uploader().upload(immagine.getBytes(),
                Map.of("folder", "immagini-veicolo"));

        return (String) result.get("secure_url");
    }
}
