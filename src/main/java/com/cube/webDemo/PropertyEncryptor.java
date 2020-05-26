package com.cube.webDemo;

import org.jasypt.encryption.StringEncryptor;

import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricStringEncryptor;
import com.ulisesbocchio.jasyptspringboot.util.AsymmetricCryptography;

public class PropertyEncryptor {

	public static void main(String[] args) {
        SimpleAsymmetricConfig config = new SimpleAsymmetricConfig();
        //config.setPublicKey("");
        config.setPublicKeyFormat(AsymmetricCryptography.KeyFormat.PEM);
        config.setPublicKeyLocation("file:config/public.key");
        
        StringEncryptor encryptor = new SimpleAsymmetricStringEncryptor(config);
        
        String message = "plain secret (UAT)";
        String encrypted = encryptor.encrypt(message);
        System.out.printf("Encrypted message %s\n", encrypted);
	}

}
