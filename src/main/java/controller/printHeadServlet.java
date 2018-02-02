package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@WebServlet("/printHeadServlet")
public class printHeadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie myCookie=new Cookie("color1","20131125");
        Cookie myCookie2=new Cookie("color2","9557.35");
        Cookie myCookie3=new Cookie("color3","pink");
        //2. 该cookie存在的时间 以秒为单位
        myCookie.setMaxAge(30000);
        myCookie2.setMaxAge(50000);

        //如果你不设置存在时间,那么该cookie将不会保存

        //3. 将该cookie写回到客户端
        Date   date   =   new   Date(System.currentTimeMillis());//获取当前时间
        System.out.println(date);
        response.addCookie(myCookie);
        response.addCookie(myCookie2);
        request.setAttribute("date",date);
        request.getRequestDispatcher("/WEB-INF/views/getHead.jsp").forward(request,response);
    }
}
