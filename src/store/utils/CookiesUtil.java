package store.utils;

import javax.servlet.http.Cookie;

public class CookiesUtil {



    /**
     * 通过名称在cookie数组获取指定的cookie
     * @param name cookie名称
     * @param cookies  cookie数组
     * @return
     */
    public static Cookie getCookieByname(String name,Cookie [] cookies){
        if (cookies!=null){
            for (Cookie cookie:cookies){
                if (name.equals(cookie.getName())){
                    return cookie;
                }
            }

        }
        return null;

    }


}
