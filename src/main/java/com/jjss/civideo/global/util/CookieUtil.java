package com.jjss.civideo.global.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Optional;

public final class CookieUtil {

    private CookieUtil() {
    }

    public static Optional<Cookie> getCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (isEmpty(cookies) || cookieName.isEmpty()) {
            return Optional.empty();
        }

        return find(cookies, cookieName);
    }

    public static void addCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }

    public static void removeCookie(HttpServletRequest req, HttpServletResponse res, @NotNull String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (isEmpty(cookies)) {
            return;
        }
        findAndRemove(res, cookies, cookieName);
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    private static boolean isEmpty(Cookie[] cookies) {
        return cookies == null || cookies.length == 0;
    }

    private static Optional<Cookie> find(Cookie[] cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    private static void findAndRemove(HttpServletResponse res, Cookie[] cookies, String cookieName) {
        find(cookies, cookieName).ifPresent((cookie) -> {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            res.addCookie(cookie);
        });
    }

}
