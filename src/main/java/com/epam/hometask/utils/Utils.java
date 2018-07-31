package com.epam.hometask.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class Utils {

    public static Properties readProperties(String filename) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = Utils.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                throw new IllegalArgumentException("Sorry, unable to find " + filename);
            }
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }

    public static File getFileFromResources(String filename) {
        try {
            return new File(new URI(Objects.requireNonNull(
                    Utils.class.getClassLoader().getResource(filename)).getFile()).getPath());

        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
