package api.dieti2024.util;


import api.dieti2024.exceptions.ApiException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageContainerUtil {


    private final BlobContainerClient blobContainerClient;

    public ImageContainerUtil(BlobContainerClient blobContainerClient) {
        this.blobContainerClient = blobContainerClient;
    }

    public String uploadImage(MultipartFile file,String nomePath) {

         try {
             // Get the image file name
             BlobClient blob = blobContainerClient.getBlobClient(nomePath);

             // Get a BlobClient to upload the image file
             blob.upload(file.getInputStream(),file.getSize(),true);
            return blob.getBlobUrl();
         }catch (Exception e){
             throw new ApiException("Errore nel caricamento dell'immagine", HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }
}
