/**
 * 
 */
package com.josepgaya.llocon.dto;

/**
 * Objecte de transferència amb informació d'un arxiu per descarregar.
 * 
 * @author josepgaya
 */
public class ArxiuDto {

	private String nom;
	private String contentType;
	private byte[] contingut;

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getContingut() {
		return contingut;
	}
	public void setContingut(byte[] contingut) {
		this.contingut = contingut;
	}

}
