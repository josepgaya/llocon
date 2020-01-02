/**
 * 
 */
package com.josepgaya.llocon.logic.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josepgaya.base.boot.logic.api.service.SessionService;
import com.josepgaya.llocon.logic.api.dto.UserSession;

/**
 * Implementació del servei encarregat de gestionar les sessions d'usuari del front.
 * 
 * @author josepgaya
 */
@Service
public class SessionServiceImpl implements SessionService {

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	@Override
	public Object parseJsonSession(JsonNode jsonNode) {
		return jacksonObjectMapper.convertValue(jsonNode, UserSession.class);
	}

	@Override
	public Object parseJwtSession(Map<String, Object> jwtSession) {
		if (jwtSession == null) {
			return null;
		} else {
			UserSession session = new UserSession();
			session.setLl(
					objectToLong(jwtSession.get("ll")));
			return session;
		}
	}

	@Override
	public List<GrantedAuthority> getAuthoritiesFromSession(String usuariCodi, Object session) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		return grantedAuthorities;
	}

	private Long objectToLong(Object o) {
		if (o == null) {
			return null;
		} else {
			if (o instanceof Number) {
				return ((Number)o).longValue();
			} else if (o instanceof String) {
				return Long.parseLong((String)o);
			} else {
				return null;
			}
		}
	}

}