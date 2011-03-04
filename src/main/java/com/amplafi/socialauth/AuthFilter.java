package com.amplafi.socialauth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.AuthProviderFactory;
import org.brickred.socialauth.Profile;

public class AuthFilter implements Filter {

	public static final String ATTR_ERROR = "error";
	public static final String ATTR_OPEN_ID = "open-id";
	private static final String ATTR_SOCIAL_AUTH = "SocialAuth";

	private static final String PARAMETER_PROVIDER_ID = "idProvider";

	private static final String URI_AUTH = "/auth";
	private static final String URI_LOGOUT = "/logout";
	
	private static final String PAGE_SECURED = "/secured.jsp";
	private static final String PAGE_LOGIN = "/index.jsp";


	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession();
		String servletPath = httpServletRequest.getServletPath();
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if(servletPath.equals(URI_LOGOUT)){
			doLogout(session);
		}
		if (isAuthenticated(session)) {
			chain.doFilter(httpServletRequest, response);
		} else {
			if (!servletPath.equals(PAGE_LOGIN)) {
				boolean socialAuthRequest = servletPath.equals(URI_AUTH);
				if (socialAuthRequest) {
					try {
						session.setAttribute(ATTR_OPEN_ID, getAuthInfo(httpServletRequest));
						showSecured(httpServletResponse);
					} catch (Exception e) {
						showError(httpServletResponse, e);
					}
				} else {
						showLogin(httpServletResponse);
				}
			} else {
				if(httpServletRequest.getParameterMap().containsKey(PARAMETER_PROVIDER_ID)){
					try{ 
					  // id can have values "facebook", "twitter", "yahoo" etc. or the OpenID URL
					  AuthProvider provider = AuthProviderFactory.getInstance(httpServletRequest.getParameter(PARAMETER_PROVIDER_ID));
					  // URL of YOUR application which will be called after authentication
					  String returnToUrl = "http://localhost:8080/auth";
					  // Store in session
					  session.setAttribute(ATTR_SOCIAL_AUTH, provider);
					  httpServletResponse.sendRedirect(provider.getLoginRedirectURL(returnToUrl));
					} catch (Exception e) {
						showError(httpServletResponse, e);
						e.printStackTrace();
					}
				} else {
					chain.doFilter(httpServletRequest, response);
				}
			}
		}
	}

	private void showError(HttpServletResponse httpServletResponse, Exception e)
			throws IOException {
		httpServletResponse.sendError(200, e.getMessage());
	}

	private void showSecured(HttpServletResponse httpServletResponse)throws IOException {
		httpServletResponse.sendRedirect(PAGE_SECURED);
	}

	private void showLogin(HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.sendRedirect(PAGE_LOGIN);
	}

	private String getAuthInfo(HttpServletRequest httpServletRequest) throws Exception {
		AuthProvider provider = (AuthProvider)httpServletRequest.getSession().getAttribute(ATTR_SOCIAL_AUTH);
		Profile p = provider.verifyResponse(httpServletRequest);
		return p.getValidatedId();
	}

	private void doLogout(HttpSession session) {
		session.removeAttribute(ATTR_OPEN_ID);
	}

	private boolean isAuthenticated(HttpSession session) {
		return session.getAttribute(ATTR_OPEN_ID) != null;
	}

	public void destroy() {

	}

}
