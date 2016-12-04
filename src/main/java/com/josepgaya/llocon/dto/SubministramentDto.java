/**
 * 
 */
package com.josepgaya.llocon.dto;

import com.josepgaya.llocon.entity.SubministramentProducteEnum;

/**
 * Objecte de transferència amb informació d'un subministrament.
 * 
 * @author josepgaya
 */
public class SubministramentDto {

	private SubministramentProducteEnum producte;
	private String contracteNum;
	private LloguerDto lloguer;

	public SubministramentProducteEnum getProducte() {
		return producte;
	}
	public void setProducte(SubministramentProducteEnum producte) {
		this.producte = producte;
	}
	public String getContracteNum() {
		return contracteNum;
	}
	public void setContracteNum(String contracteNum) {
		this.contracteNum = contracteNum;
	}
	public LloguerDto getLloguer() {
		return lloguer;
	}
	public void setLloguer(LloguerDto lloguer) {
		this.lloguer = lloguer;
	}

}
