package cn.qtec.bbs.config;

import cn.qtec.bbs.controller.*;
import cn.qtec.bbs.entity.model.*;
import cn.qtec.bbs.kit.EJ;
import com.jfinal.config.*;
import com.jfinal.core.Const;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * Created by wuzh on 2019/1/2.
 */
public class AppJFConfig extends JFinalConfig {
    // 配置常量
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        // TODO Auto-generated method stub
        me.setError404View("404.html");// 设置404页面
        me.setMaxPostSize(Const.DEFAULT_MAX_POST_SIZE);// 设置文件大小,默认为10MB
        me.setI18nDefaultLocale("zh_CN");
    }

    // 配置接管所有的web请求
    @Override
    public void configHandler(Handlers me) {
        // TODO Auto-generated method stub

    }

    // 配置全局拦截器
    @Override
    public void configInterceptor(Interceptors me) {
        // TODO Auto-generated method stub

    }

    // 数据库配置
    @Override
    public void configPlugin(Plugins me) {
        //配置数据源
        DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost:3306/redbbs",
                "root", "root");
        me.add(dp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        //关联数据库表和Model
        arp.addMapping("im_imgroup", ImgGroup.class);
        arp.addMapping("im_partner", Partner.class);
        arp.addMapping("sys_actlog", ActLog.class);
        arp.addMapping("sys_comment", Comment.class);
        arp.addMapping("sys_content", Content.class);
        arp.addMapping("sys_dynattr", DynAttr.class);
        arp.addMapping("sys_userrecord", UserRecord.class);
        me.add(arp);
    }

    @Override
    public void configRoute(Routes me) {
        // TODO Auto-generated method stub
        me.add("/", IndexController.class);
        me.add("/project", ProjectController.class);
        me.add("/article", ArticleController.class);
        me.add("/user", UserController.class);
        me.add("/jie", JieController.class);
        me.add("/column", ColumnController.class);
        me.add("/comment", CommentController.class);
        me.add("/file",FileController.class);
    }

    // 配置引擎模板
    @Override
    public void configEngine(Engine me) {
        // devMode 配置为 true，将支持模板实时热加载
        me.setDevMode(true);
        //从 class path 和 jar 包加载模板配置
        me.setBaseTemplatePath(System.getProperty("user.dir")+"\\src\\main\\resources\\templates");
        me.addSharedObject("EJ", new EJ());
        // 添加共享函数，随后可在任意地方调用这些共享函数
        me.addSharedFunction("/_t/layout.html");
    }

}
