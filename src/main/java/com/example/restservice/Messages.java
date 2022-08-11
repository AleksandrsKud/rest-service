package com.example.restservice;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final String BUNDLE_NAME = "com.example.restservice.message";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    private Messages() {
    }

    public static String getString( String key ) {
        try {
                return RESOURCE_BUNDLE.getString( key );
            } catch (MissingResourceException e) {
                return '!' + key + '!';
            }
    }

    public static String format (String key, Object arg0) {
        return format (key, new Object [] {arg0});
    }

    public static String format (String key, Object[] arguments) {
        return MessageFormat.format(getString (key), arguments);
    }
}