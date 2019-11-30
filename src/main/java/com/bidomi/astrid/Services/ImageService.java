package com.bidomi.astrid.Services;

import com.bidomi.astrid.Model.Unit;
import com.bidomi.astrid.Model.UnitImage;
import com.bidomi.astrid.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ImageService {
    @Value("${users-images-path}")
    private String usersImagesPath;
    @Value("${units-images-path}")
    private String unitsImagesPath;

    public ImageService() {
    }

    public User fillUsersImage(User user) {
        if (user.getImage() != null) {
            System.out.println("fillUsersImage: \n" + user);
            File f = new File("" + usersImagesPath + user.getImage().getFilename());
            String encodstring = encodeFileToBase64Binary(f);
            user.getImage().setValue(encodstring);
        }
        return user;
    }

    public User fillUsersUnitsImages(User user) {
        if (user.getUnits() != null) {
            user.getUnits().forEach((Unit unit) -> {
                if (unit.getImages().size() > 0) {
                    fillUnitsImages(unit);
                }
            });
        }
        return user;
    }

    public Unit fillUnitsImages(Unit unit) {
        unit.getImages().forEach((UnitImage image) -> {
            File f = new File("" + unitsImagesPath + image.getFilename());
            String encodstring = encodeFileToBase64Binary(f);
            image.setValue(encodstring);
        });
        return unit;
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(bytes), "UTF-8");
            fileInputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }
}
