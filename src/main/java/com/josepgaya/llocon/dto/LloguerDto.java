/**
 * 
 */
package com.josepgaya.llocon.dto;

/**
 * Objecte de transferència amb informació d'un lloguer.
 * 
 * @author josepgaya
 */
public class LloguerDto {

	private String codi;
	private String nom;
	private String adressa;

	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getAdressa() {
		return adressa;
	}
	public void setAdressa(String adressa) {
		this.adressa = adressa;
	}

}
