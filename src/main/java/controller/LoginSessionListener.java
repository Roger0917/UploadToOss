package controller;

import entity.PersonInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import java.util.HashMap;
import java.util.Map;

@WebListener

public class LoginSessionListener implements HttpSessionAttributeListener {
    Log log= LogFactory.getLog(this.getClass());
    Map<String,HttpSession> map=new HashMap<String,HttpSession>();

    // Public constructor is required by servlet spec
    public LoginSessionListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
      /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------
    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
      String name=sbe.getName();
      if(("personInfo").equals(name)){
          PersonInfo personInfo=(PersonInfo) sbe.getValue();
          if(map.get(personInfo.getAccount())!=null){
              //map中有记录,表明该账号在其他机器上登陆过,将以前的登陆失效
              HttpSession session=map.get(personInfo.getAccount());
              PersonInfo oldPersonInfo=(PersonInfo) session.getAttribute("personInfo"); //map中已存在的旧信息
              log.info("账号"+oldPersonInfo.getAccount()+"在"+oldPersonInfo.getIp()+"已经登陆,该登陆将被迫下线");
              session.removeAttribute("personInfo");
              session.setAttribute("msg","您的账号已经在其他机器上登陆,您被迫下线");
              //将session以用户名为索引,放入map中
              map.put(personInfo.getAccount(),sbe.getSession());
              log.info("账号"+personInfo.getAccount()+"在"+personInfo.getIp()+"登陆");

          }
      }
    }
    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
      String name=sbe.getName();

      //注销
        if(name.equals("personInfo")){
            //将该session从map移除
            PersonInfo personInfo=(PersonInfo) sbe.getValue();
            map.remove(personInfo.getAccount());
            log.info("账号"+personInfo.getAccount());
        }
    }
    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
      String name=sbe.getName();

      //没有注销的情况下,用另一个账号登陆
        if(name.equals("personInfo")){

            //移除旧的登陆信息
            PersonInfo oldPersonInfo=(PersonInfo) sbe.getValue();
            map.remove(oldPersonInfo.getAccount());

            //新的登陆信息
            PersonInfo personInfo=(PersonInfo) sbe.getSession().getAttribute("personInfo");

            //也要检查新登陆的账号是否在别的机器上登陆过
            if(map.get(personInfo.getAccount())!=null){

                //map中有记录,表明该账号在其他机器上登陆过,将以前的登陆失效
                HttpSession session=map.get(personInfo.getAccount());
                session.removeAttribute("personInfo");
                session.setAttribute("msg","您的账号已经在其他机器上登陆,您被迫下线");
            }
            map.put("personInfo",sbe.getSession());
        }
    }
}
