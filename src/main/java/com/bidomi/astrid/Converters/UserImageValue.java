package com.bidomi.astrid.Converters;

import com.bidomi.astrid.Model.UserImage;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UserImageValue extends StdConverter<UserImage, UserImage> {
    @Value("${users-images-path}")
    private String usersImagesPath;
    @Override
    public UserImage convert(UserImage image) {
//        System.out.println("ImageValue: " + image);
            File f =  new File(""+ usersImagesPath +image.getFilename());
            String encodstring = encodeFileToBase64Binary(f);
            image.setValue(encodstring);
//        System.out.println("class ImageValue extends StdConverter" + encodstring);
            return image;
    }
    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            fileInputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }
}
