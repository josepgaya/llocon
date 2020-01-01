/**
 * 
 */
package com.josepgaya.llocon.logic.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.josepgaya.base.boot.logic.service.AbstractGenericServiceImpl;
import com.josepgaya.llocon.core.entity.ConnexioEntity;
import com.josepgaya.llocon.core.exception.GenericServiceException;
import com.josepgaya.llocon.logic.api.dto.Connexio;
import com.josepgaya.llocon.logic.api.service.ConnexioService;

/**
 * Implementació del service per a la gestió de connexions.
 * 
 * @author josepgaya
 */
@Service
public class ConnexioServiceImpl extends AbstractGenericServiceImpl<Connexio, ConnexioEntity, Long> implements ConnexioService {

	@Override
	protected void beforeCreate(ConnexioEntity entity, Connexio dto) {
		try {
			dto.setContrasenya(xifrarContrasenya(dto.getContrasenya()));
		} catch (GeneralSecurityException ex) {
			throw new GenericServiceException(
					"Error al encriptar la contrasenya",
					ex);
		}
		entity.update(dto);
	}

	private static final String CLAU_XIFRAT = "L1oc0n";
	private static String xifrarContrasenya(
			String contrasenya) throws GeneralSecurityException {
		byte[] bytes = contrasenya.getBytes(StandardCharsets.UTF_8);
		Cipher cipher = Cipher.getInstance("RC4");
		SecretKeySpec rc4Key = new SecretKeySpec(CLAU_XIFRAT.getBytes(),"RC4");
		cipher.init(Cipher.ENCRYPT_MODE, rc4Key);
		byte[] xifrat = cipher.doFinal(bytes);
		return Base64.getEncoder().encodeToString(xifrat);
	}
	@SuppressWarnings("unused")
	private static String desxifrarContrasenya(String contrasenyaXifrada) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("RC4");
		SecretKeySpec rc4Key = new SecretKeySpec(CLAU_XIFRAT.getBytes(),"RC4");
		cipher.init(Cipher.DECRYPT_MODE, rc4Key);
		byte[] desxifrat = cipher.doFinal(Base64.getDecoder().decode(contrasenyaXifrada));
		return new String(desxifrat, StandardCharsets.UTF_8);
	}

}
