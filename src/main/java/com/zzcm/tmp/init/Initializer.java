package com.zzcm.tmp.init;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * Created by Administrator on 2015/12/25.
 */
public class Initializer implements WebApplicationInitializer{
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebAppConfig.class);
        servletContext.addListener(new ContextLoaderListener(ctx));

        ctx.setServletContext(servletContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        FilterRegistration.Dynamic encodingServlet = servletContext.addFilter("encodingFilter", encodingFilter);
        encodingServlet.addMappingForUrlPatterns(null,true,"/*");

        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistration.Dynamic webStatServlet = servletContext.addFilter("DruidWebStatFilter", webStatFilter);
        webStatServlet.setInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        webStatServlet.addMappingForUrlPatterns(null,true,"/*");

        ServletRegistration.Dynamic druidStatViewServlet = servletContext.addServlet("DruidStatView",new StatViewServlet());
        druidStatViewServlet.addMapping("/druid/*");


    }
}
