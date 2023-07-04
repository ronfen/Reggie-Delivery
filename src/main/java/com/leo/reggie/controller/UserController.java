package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.User;
import com.leo.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * send msg to a phone number for verification, but in this case, we use hard code instead.
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phoneNum = user.getPhone();

        if(StringUtils.isNotEmpty(phoneNum)){
            int code = 8888;
            session.setAttribute(phoneNum, code);

            log.info("============= {}", code);

            return R.success("Message has been sent successfully!");
        }

        return R.error("Code is not sent.");
    }


    /**
     * map is for phone and code
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = session.getAttribute(phone);
        if(codeInSession != null && codeInSession.equals(codeInSession)){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);

            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            return R.success(user);
        }


        return R.error("Message failed to be sent!");

    }
}
