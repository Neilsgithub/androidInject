package com.wangjie.androidinject.annotation.core.base;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidinject.annotation.core.base.process.AIAnnotationProcessor;
import com.wangjie.androidinject.annotation.present.AIPresent;
import com.wangjie.androidinject.annotation.present.common.AnnoProcessorAlias;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wangjie  email: tiantian.china.2@gmail.com
 * Date: 13-11-30
 * Time: 下午7:23
 * To change this template use File | Settings | File Templates.
 */
public class RealizeMethodAnnotation implements RealizeAnnotation {
    private static final String TAG = RealizeMethodAnnotation.class.getSimpleName();
    private static Map<Class<?>, RealizeMethodAnnotation> map = new HashMap<Class<?>, RealizeMethodAnnotation>();

    public synchronized static RealizeMethodAnnotation getInstance(AIPresent present) {
        Class clazz = present.getClass();
        RealizeMethodAnnotation realize = map.get(clazz);
        if (null == realize) {
            realize = new RealizeMethodAnnotation();
            map.put(clazz, realize);
        }
        realize.setPresent(present);
        realize.setClazz(clazz);
        return realize;
    }


    private AIPresent present;
    private Class<?> clazz;

    @Override
    public void processAnnotation() throws Exception {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends AIAnnotationProcessor> processorClass = AnnoProcessorAlias.getAnnotationProcessor(annotation.annotationType());
                if (null == processorClass) {
                    continue;
                }
                AIAnnotationProcessor processor = processorClass.newInstance();
                processor.process(present, method);
            }

            present.parserMethodAnnotations(method);
        }


    }


    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setPresent(AIPresent present) {
        this.present = present;
    }


}
