package cn.qtec.bbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;


/**
 * Created by wuzh on 2019/1/16.
 */
public class FileController extends Controller {

    public void upload() {
//        HttpServletRequest request = getRequest();
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        List<FileItem> items = upload.parseRequest(request);
//
//        System.out.println(upload.getHeaderEncoding());
        UploadFile file = getFile();
        JSONObject object = new JSONObject();
        object.put("retcode",1);
        object.put("retinfo","黑白了");
        renderJson(object);
    }

}
