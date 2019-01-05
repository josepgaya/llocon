/**
 * 
 */
package com.josepgaya.llocon.front.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configuració per a l'autenticació dels usuaris.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*private static final boolean LDAP_ACTIU = true;

	@Value("${cecogest.ldap.server.url}")
    private String ldapServerUrl;
	@Value("${cecogest.ldap.manager.dn}")
    private String ldapManagerDn;
	@Value("${cecogest.ldap.manager.password}")
    private String ldapManagerPassword;
	@Value("${cecogest.ldap.user.search.base}")
    private String ldapUserSearchBase;
	@Value("${cecogest.ldap.user.search.filter}")
    private String ldapUserSearchFilter;
	@Value("${cecogest.ldap.group.search.base}")
    private String ldapGroupSearchBase;
	@Value("${cecogest.ldap.group.search.filter}")
    private String ldapGroupSearchFilter;
	@Value("${cecogest.ldap.group.search.attr}")
    private String ldapGroupSearchAttr;*/

	/*@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.
		inMemoryAuthentication().
		withUser("user").password("{noop}user").authorities("ROLE_USER");
	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		httpBasic().
		and().
		authorizeRequests().
		antMatchers(
				"/user",
				"/comu/api/**/*",
				"/facturacio/api/**/*",
				"/estudispro/api/**/*").authenticated().
		anyRequest().permitAll().
		and().
		csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		http.headers().frameOptions().sameOrigin();
		http.csrf().disable();
	}

	/*@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (LDAP_ACTIU) {
			auth.
			ldapAuthentication().
				userSearchBase(ldapUserSearchBase).
				userSearchFilter(ldapUserSearchFilter).
		        groupSearchBase(ldapGroupSearchBase).
		        groupSearchFilter(ldapGroupSearchFilter).
		        groupRoleAttribute(ldapGroupSearchAttr).
			contextSource().
				url(ldapServerUrl).
				managerDn(ldapManagerDn).
				managerPassword(ldapManagerPassword);
		}
	}*/

}
