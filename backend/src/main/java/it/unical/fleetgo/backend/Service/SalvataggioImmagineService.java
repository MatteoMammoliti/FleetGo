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
}