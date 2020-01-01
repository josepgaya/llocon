/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.base.boot.back.controller.AbstractIdentificableApiController;
import com.josepgaya.base.boot.logic.api.controller.GenericController;
import com.josepgaya.llocon.logic.api.dto.Lloguer;

/**
 * Controlador pel servei REST de gestió de lloguers.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping(GenericController.API_PATH + "/lloguers")
public class LloguerApiController extends AbstractIdentificableApiController<Lloguer, Long> {

}
