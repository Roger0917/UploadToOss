package util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author Jay
 */
public class AliyunOssClientUtil {
    //log日志 /** */
    protected static Logger logger=Logger.getLogger(AliyunOssClientUtil.class);
    //阿里云api的内或外网域名
    private static String ENDPOINT;
    //阿里云api的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云api的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云api的bucket名称
    private  static String BUCKET_NAME;
    //阿里云api的文件夹名称
    private static String FOLDER;

    //初始化属性
    static{
        ENDPOINT=OssClientConstants.ENDPOINT;
        ACCESS_KEY_ID=OssClientConstants.ACCESS_KEY_ID;
        ACCESS_KEY_SECRET=OssClientConstants.Access_KEY_SECRET;
        BUCKET_NAME=OssClientConstants.BUCKET_NAME;
        FOLDER=OssClientConstants.FOLDER;
    }

    /**
     * 获取阿里云OSS客户端对象
     */
    public static OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID,ACCESS_KEY_SECRET);
    }

    /**
     * @Author:Jay
     * @Description:
     * @Date: 14:23 2018/1/18 0018
     * @param ossClient oss连接
     * @param bucketName 存储空间
     *                  `
    */
    public static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames=bucketName;
        if(ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间bucketName
     * @param ossClient oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient,String bucketName){
        ossClient.deleteBucket(bucketName);
        logger.info("删除"+bucketName+"Bucket成功");
    }
    /**
     * 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder 模拟文件夹名如"jay/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient,String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash=folder;
        //判断文件夹是否存在,不存在则创建
        if(!ossClient.doesObjectExist(bucketName,keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName,keySuffixWithSlash,new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object=ossClient.getObject(bucketName,keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder 模拟文件夹名如 "jay/"
     * @param key Bucket下的文件的路径名+文件名 如:"upload/cake.jpg"
     */

    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key){
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }

    /**
     *
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder 模拟文件夹名如 "jay/"
     * @return String 返回的唯一MD5数字签名
     */
    public static String uploadObject2OSS(HttpServletRequest request, HttpServletResponse response, OSSClient ossClient, InputStream is, String fileName, long fileSize, String bucketName, String folder) {
        String resultStr = null;
        String key = "object-get-progress-sample";
        try {
            //以输入流的形式上传文件
            /*InputStream is = new FileInputStream(file);
            //文件名
            String fileName = file.getName();
            //文件大小
            Long fileSize = file.length();*/
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME,定义文件的类型及网页编码,决定浏览器将以什么形式,什么编码读取文件,如果用户没有指定则根据key或文件名的扩展名生成
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType("application/octet-stream");
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            //PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            PutObjectResult putResult=ossClient.putObject(new PutObjectRequest(bucketName, folder + fileName, is, metadata).
                    <PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
            //解析结果
            resultStr = putResult.getETag();
            System.out.println("上传结果"+resultStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

   public static class PutObjectProgressListener implements ProgressListener {
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;
        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to upload......");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                        System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");

                    } else {
                        System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }
        public boolean isSucceed() {
            return succeed;
        }
    }

        /**
         * 通过文件名判断并获取OSS服务文件上传时文件的contentType
         * @param fileName 文件名
         * @return 文件的contentType
         */
        public static String getContentType(String fileName){
            //文件的后缀名
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if(".bmp".equalsIgnoreCase(fileExtension)) {
                return "image/bmp";
            }
            if(".gif".equalsIgnoreCase(fileExtension)) {
                return "image/gif";
            }
            if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
                return "image/jpeg";
            }
            if(".html".equalsIgnoreCase(fileExtension)) {
                return "text/html";
            }
           if(".txt".equalsIgnoreCase(fileExtension)) {
                return "text/plain";
            }
            if(".vsd".equalsIgnoreCase(fileExtension)) {
                return "application/vnd.visio";
            }
            if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
                return "application/vnd.ms-powerpoint";
            }
            if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
                return "application/msword";
            }
            if(".xml".equalsIgnoreCase(fileExtension)) {
                return "text/xml";
            }
            //默认返回类型
            return "image/jpeg";
        }
    public static void main(String[] args) {
        //初始化OSSClient
        OSSClient ossClient=AliyunOssClientUtil.getOSSClient();
        //上传文件
        String files="K:\\大学\\UserData\\My Documents\\My Pictures\\热巴\\001.jpg,K:\\大学\\UserData\\My Documents\\My Pictures\\热巴\\006.jpg";
        String[] file=files.split(",");
        for(String filename:file){
            //System.out.println("filename:"+filename);
            File filess=new File(filename);
           // String md5key = AliyunOSSClientUtil.uploadObject2OSS(ossClient, filess, BUCKET_NAME, FOLDER);
            //logger.info("上传后的文件MD5数字唯一签名:" + md5key);
            //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
        }

    }

}

