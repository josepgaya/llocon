/**
 * 
 */
package com.josepgaya.llocon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.scrap.Factura;
import com.josepgaya.llocon.service.SubministramentService;

/**
 * Controlador per als subministraments.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping("/subministrament")
public class SubministramentController {

	@Autowired
	private SubministramentService subministramentService;
	
	@RequestMapping(path = "/{subministramentId}/actualitzar")
	public List<Factura> actualitzar(
			@PathVariable Long subministramentId) {
		return subministramentService.actualitzarFactures(subministramentId);
	}

}
