/**
 * 
 */
package com.josepgaya.llocon.core.dto;

/**
 * Objecte de transferència amb informació d'un arxiu per descarregar.
 * 
 * @author josepgaya
 */
public class File {

	private String name;
	private String contentType;
	private byte[] content;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getLength() {
		return (content != null) ? content.length : 0;
	}

}
