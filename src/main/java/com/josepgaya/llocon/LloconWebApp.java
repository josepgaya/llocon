/**
 * 
 */
package com.josepgaya.llocon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicació de control de factures de lloguers emprant spring-boot.
 * 
 * @author josepgaya
 */
@SpringBootApplication
public class LloconWebApp {

	public static void main(String[] args) {
		SpringApplication.run(
				LloconWebApp.class,
				args);
	}

}
