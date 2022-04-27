package com.jjss.civideo.global.config.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		filterChain.doFilter(requestWrapper, responseWrapper);
		stopWatch.stop();

		response.setCharacterEncoding("UTF-8");

		log.info("[{} {}]\n" +
				"HTTP Status Code: {}\n" +
				"Time: {} sec\n" +
				"Headers: {}\n" +
				"Request: {}\n" +
				"Response: {}\n",
			request.getMethod(),
			request.getRequestURI(),
			responseWrapper.getStatus(),
			stopWatch.getTotalTimeSeconds(),
			getHeaders(request),
			getRequestBody(requestWrapper),
			getResponseBody(responseWrapper));
	}

	private Map<String, Object> getHeaders(HttpServletRequest request) {
		Map<String, Object> headerMap = new HashMap<>();
		Enumeration<String> headerArray = request.getHeaderNames();
		while (headerArray.hasMoreElements()) {
			String headerName = headerArray.nextElement();
			headerMap.put(headerName, request.getHeader(headerName));
		}

		return headerMap;
	}

	private String getRequestBody(ContentCachingRequestWrapper request) {
		if (request != null) {
			byte[] buf = request.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					return new String(buf, 0, buf.length, request.getCharacterEncoding());
				} catch (UnsupportedEncodingException ignored) {
				}
			}
		}

		return "[]";
	}

	private String getResponseBody(ContentCachingResponseWrapper response) throws IOException {
		String payload = null;
		if (response != null) {
			byte[] buf = response.getContentAsByteArray();
			if (buf.length > 0) {
				payload = new String(buf, 0, buf.length, response.getCharacterEncoding());
				response.copyBodyToResponse();
			}
		}

		return payload == null ? "[]" : payload;
	}

}
