package com.bidomi.astrid.Converters;

import com.bidomi.astrid.Model.UnitImage;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public class ImageValue extends StdConverter<Collection<UnitImage>, Collection<UnitImage>> {
    @Value("${units-images-path}")
    private String unitsImagesPath;

    @Override
    public Collection<UnitImage> convert(Collection<UnitImage> images) {
//        System.out.println("ImageValue: " + images);
        for (UnitImage image : images) {
            File f = new File("" + unitsImagesPath + image.getFilename());
            String encodstring = encodeFileToBase64Binary(f);
            image.setValue(encodstring);
        }
        return images;
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
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
