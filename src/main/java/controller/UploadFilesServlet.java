package controller;

import com.aliyun.oss.OSSClient;
import util.AliyunOssClientUtil;
import util.OssClientConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jay
 */
@WebServlet("/UploadFilesServlet")
@MultipartConfig
public class UploadFilesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //初始化OSSClient
        OSSClient ossClient=AliyunOssClientUtil.getOSSClient();
        for(Part part:req.getParts()){
            if(part!=null&&part.getName().startsWith("file")){
                InputStream is=part.getInputStream();
                String fname=part.getSubmittedFileName();
                long fileSize=part.getSize();
                System.out.println("文件名"+fname+"文件类型"+part.getContentType()+"文件大小"+fileSize);
                String md5key= AliyunOssClientUtil.uploadObject2OSS(req,resp,ossClient,is,fname,fileSize, OssClientConstants.BUCKET_NAME,OssClientConstants.FOLDER);
                is.close();
                //logger.info("上传后的文件MD5数字唯一签名:" + md5key);
                //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
            }
        }
        ossClient.shutdown();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String uploadFile="uploadFile";
        String methodName = request.getParameter("method");
        if(uploadFile.equals(methodName)){
            request.getRequestDispatcher("/WEB-INF/views/uploadFiles.jsp").forward(request, response);
        }else {
            doPost(request,response);
        }
    }
}

