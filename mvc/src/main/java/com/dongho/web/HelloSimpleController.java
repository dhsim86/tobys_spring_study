package com.dongho.web;

import com.dongho.config.mvc.annotation.RequiredParams;
import com.dongho.config.mvc.annotation.ViewName;
import com.dongho.config.mvc.controller.SimpleController;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("/simple-hello")
public class HelloSimpleController implements SimpleController {

    @ViewName("/simple-hello")
    @RequiredParams({"name"})
    @Override
    public void control(Map<String, String> params, Map<String, Object> model) {
        model.put("message", "Hello " + params.get("name"));
    }

}
