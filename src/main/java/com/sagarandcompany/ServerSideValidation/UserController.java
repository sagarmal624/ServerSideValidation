package com.sagarandcompany.ServerSideValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public Object handlePostRequest(@Valid @ModelAttribute("user") User user,
                                    BindingResult bindingResult, Model model) {
        new UserValidator().validate(user, bindingResult);
        Map map = new HashMap();
        if (bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    String message = messageSource.getMessage(fieldError, null);
                    map.put(fieldError.getField(), message);
                }
            }
            return map;
        }

        return user;
    }

}
