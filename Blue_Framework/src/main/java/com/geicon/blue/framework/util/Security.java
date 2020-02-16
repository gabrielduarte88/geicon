package com.geicon.blue.framework.util;

import com.geicon.blue.framework.exceptions.IllegalParametersException;
import com.geicon.blue.framework.exceptions.InvalidConfigurationException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Criptografia
 *
 * @author Gabriel Duarte
 */
public class Security {
    /**
     * Chave de segurança
     */
    private static String securityKey;
    private static StandardPBEStringEncryptor encryptor;

    /**
     * Codificar um texto
     *
     * @param text Texto
     * @return Texto codificado
     */
    public static String encrypt(String text) {
        if (encryptor == null) {
            if (securityKey == null) {
                try {
                    ResourceBundle rb = ResourceBundle.getBundle("security");
                    securityKey = rb.getString("security.key");
                }
                catch (MissingResourceException ex) {
                    throw new InvalidConfigurationException("Security key not found in config.properties", ex);
                }
            }

            encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm("PBEWithMD5AndDES");
            encryptor.setPassword(securityKey);
            encryptor.setStringOutputType("hexadecimal");
        }

        return encryptor.encrypt(text);
    }

    /**
     * Decodificar um texto
     *
     * @param code Texto codificado
     * @return Texto original
     */
    public static String decrypt(String code) {
        if (encryptor == null) {
//            if (securityKey == null) {
//                try {
//                    ResourceBundle rb = ResourceBundle.getBundle("security");
//                    securityKey = rb.getString("security.key");
//                }
//                catch (MissingResourceException ex) {
//                    throw new InvalidConfigurationException("Security key not found in config.properties", ex);
//                }
//            }

            encryptor = new StandardPBEStringEncryptor();
            encryptor.setAlgorithm("PBEWithMD5AndDES");
            encryptor.setPassword("745575ba9ecabd35");
            encryptor.setStringOutputType("hexadecimal");
        }

        return encryptor.decrypt(code);
    }

    /**
     * Transforma uma string em hash SHA-256 (Base 64)
     *
     * @param data String original
     * @return Hash gerada
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String hash(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (data == null) {
            throw new IllegalParametersException("Missing hash generation parameters (data or algorithm).");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.update(data.getBytes());

        return new String(Base64.getEncoder().encode(digest.digest()));
    }

    /**
     * Construtor
     */
    private Security() {
    }

    public static void main(String[] args) {
        System.out.println(Security.decrypt("e053957ab0c828278192167e0343339683fa467ae1457ccae9e608a5329dd331"));
    }
}
