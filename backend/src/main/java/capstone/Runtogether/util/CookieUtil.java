package capstone.Runtogether.util;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieUtil {

    public Cookie createCookie(String cookieName, String value){
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true);
        token.setMaxAge(60*60);
        token.setPath("/");
        //tokenCookie.setSecure(true);

        return token;
    }

    public Cookie getCookie(HttpServletRequest req,String CookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(CookieName))
                return cookie;
        }
        return null;
    }
}
