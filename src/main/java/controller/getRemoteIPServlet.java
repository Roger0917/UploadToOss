package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;

@WebServlet("/getRemoteIPServlet")
public class getRemoteIPServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = request.getParameter("method");
        if(("setCookie").equals(methodName)){
            request.getRequestDispatcher("/WEB-INF/views/setCookie.jsp").forward(request, response);
        }
        response.setContentType("text/html;charset=utf-8");
        //指定该页面不缓存
        //response.setDateHeader("Expires",-1); //IE游览器支持的

        //缓存一定的时间  缓存 一天的时间
        response.setDateHeader("Expires",System.currentTimeMillis()+30*1000);
        //保证兼容性
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragme", "no-cache");
        System.out.println("客户端真实地址"+getRemortIP(request));
        response.getWriter().println("客户端真实地址"+getRemortIP(request)+"<br />");
        getHttpHeader(request,response);
        //getCookie(request,response);
        /**
         * Servlet设置cookie
         */

        // 为名字和姓氏创建 Cookie
        Cookie name = new Cookie("name",
                URLEncoder.encode(request.getParameter("name"), "UTF-8")); // 中文转码
        Cookie url = new Cookie("url",
                request.getParameter("url"));

        // 为两个 Cookie 设置过期日期为 24 小时后
        name.setMaxAge(60*60*24);
        url.setMaxAge(60*60*24);

        // 在响应头中添加两个 Cookie
        response.addCookie( name );
        response.addCookie( url );

        PrintWriter out = response.getWriter();
        String title = "设置 Cookie 实例";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>站点名：</b>："
                + request.getParameter("name") + "\n</li>" +
                "  <li><b>站点 URL：</b>："
                + request.getParameter("url") + "\n</li>" +
                "</ul>\n" +
                "</body></html>");

    }
    /**
     *  Servlet读取cookie
     */
   /* public void getCookie(HttpServletRequest request,HttpServletResponse response){
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取与该域相关的 Cookie 的数组
        cookies = request.getCookies();

        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();


            String title = "Delete Cookie Example";
            String docType = "<!DOCTYPE html>\n";
            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + title + "</title></head>\n" +
                    "<body bgcolor=\"#f0f0f0\">\n" );
            if( cookies != null ){
                out.println("<h2>Cookie 名称和值</h2>");
                for (int i = 0; i < cookies.length; i++){
                    cookie = cookies[i];
                    if((cookie.getName( )).compareTo("name") == 0 ){
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        out.print("已删除的 cookie：" +
                                cookie.getName( ) + "<br/>");
                    }
                    out.print("名称：" + cookie.getName( ) + "，");
                    //out.print("值：" +  URLDecoder.decode(cookie.getValue(), "utf-8") +" <br/>");
                }
            }else{
                out.println(
                        "<h2 class=\"tutheader\">No Cookie founds</h2>");
            }
            out.println("</body>");
            out.println("</html>");
        }*/

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return System.currentTimeMillis();
    }
    //获得客户端真实ip地址方法1
    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
    //获得客户端真实ip地址方法2
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    //打印所有Header
    public static void getHttpHeader(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //返回HTTP Header头参数的名称的枚举
        Enumeration HeaderNames=request.getHeaderNames();
        while (HeaderNames.hasMoreElements()){
            //获取每个头参数的名称
            String HeaderName=(String)HeaderNames.nextElement();
            //获取参数对应的值
            String HeaderValue=request.getHeader(HeaderName);
            System.out.println("HeaderName"+HeaderName+"HeaderValue"+HeaderValue);
            response.getWriter().println("HeaderName"+HeaderName+"HeaderValue"+HeaderValue+"<br />");
        }
    }

}
