/**
 * 
 */
package com.josepgaya.llocon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador per a la pàgina inicial.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
public class IndexController {

	@RequestMapping(value = {"*"})
	public String other() {
		return "index";
	}

}
