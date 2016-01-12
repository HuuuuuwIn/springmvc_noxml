package com.zzcm.tmp.init;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
@Configuration
//@EnableWebMvc
//@ComponentScan(basePackageClasses = WebAppConfig.class)
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("json", MediaType.valueOf("application/json"));
        configurer.mediaType("xml",MediaType.valueOf("application/xml"));
        configurer.mediaType("html",MediaType.valueOf("text/html"));
        configurer.mediaType("*",MediaType.valueOf("*/*"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(new MediaType("text","plain",Charset.forName("UTF-8")));
        list.add(new MediaType("*","*",Charset.forName("UTF-8")));
        stringConverter.setSupportedMediaTypes(list);

        FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter();
        List<MediaType> jsonList = new ArrayList<MediaType>();
        jsonList.add(MediaType.valueOf("application/json;charset=UTF-8"));
        jsonList.add(MediaType.valueOf("text/plain;charset=utf-8"));
        jsonList.add(MediaType.valueOf("text/html;charset=utf-8"));
        jsonConverter.setSupportedMediaTypes(jsonList);
        jsonConverter.setFeatures(new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});

        converters.add(stringConverter);
        converters.add(jsonConverter);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
}
