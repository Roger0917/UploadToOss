package controller;

import entity.student;
import redis.clients.jedis.Jedis;
import util.RedisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "studentServlet")
public class studentServlet extends HttpServlet {
    private static Jedis jedis;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       connectRedis();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    /**
     * 连接Redis服务器
     */
    public static void connectRedis(){
        jedis= RedisUtil.getJedis();
    }
    /**
     * redis操作map
     */
    //添加学生
    public static void addStudent(String id,String name,Date birthday,String description,int average){
        student stu=new student(id,name,birthday,description,average);
        Map<String,String> map=new HashMap<String, String>();
        map.put("name",name);
        map.put("birthday",birthday.toString());
        map.put("description",description);
        map.put("average",average+"");
        jedis.hmset(id,map);
        List<String>rsmap=jedis.hmget(id,"name","birthday","description","average");
        for (String value:
             rsmap) {
            System.out.println(value);
        }
    }
    //获取学生列表
    public static void getStudent(){
        //jedis.hkeys
    }
    //根据id删除学生
    public void deleteStudent(String id){
       Long result=jedis.del(id);
        System.out.println(result);

    }
    //根据id修改学生
    public void updateStudent(String id,String name,Date birthday,String description,int average){
        Map<String,String>map=jedis.hgetAll(id);
        map.put("name",name);
        map.put("birthday",birthday.toString());
        map.put("description",description);
        map.put("average",average+"");
        jedis.hmset(id,map);
        List<String>rsmap=jedis.hmget(id,"name","birthday","description","average");
        for (String value:
                rsmap) {
            System.out.println(value);
        }
    }


}
