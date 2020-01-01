/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.base.boot.back.controller.AbstractIdentificableApiController;
import com.josepgaya.base.boot.logic.api.controller.GenericController;
import com.josepgaya.llocon.logic.api.dto.Connexio;

/**
 * Controlador pel servei REST de gestió de connexions.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping(GenericController.API_PATH + "/connexions")
public class ConnexioApiController extends AbstractIdentificableApiController<Connexio, Long> {

}
