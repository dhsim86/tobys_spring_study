package com.dongho.config.mvc.adapter;

import com.dongho.config.mvc.annotation.RequiredParams;
import com.dongho.config.mvc.annotation.ViewName;
import com.dongho.config.mvc.controller.SimpleController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof SimpleController;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method m = ReflectionUtils.findMethod(handler.getClass(), "control", Map.class, Map.class);
        ViewName viewName = AnnotationUtils.getAnnotation(m, ViewName.class);
        RequiredParams requiredParams = AnnotationUtils.getAnnotation(m, RequiredParams.class);

        Map<String, String> params = new HashMap<>();
        for (String param : requiredParams.value()) {
            String value = request.getParameter(param);

            if (value == null) {
                throw new IllegalStateException();
            }

            params.put(param, value);
        }

        Map<String, Object> model = new HashMap<>();
        SimpleController.class.cast(handler).control(params, model);

        return new ModelAndView(viewName.value(), model);
    }

    @Override
    public long getLastModified(HttpServletRequest httpServletRequest, Object o) {
        return -1;
    }

}
