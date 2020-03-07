package com.service.applehip.domain.users;

import com.service.applehip.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class GoogleLoginController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model){

        SessionUser googleUser = (SessionUser) httpSession.getAttribute("user");

        if(googleUser != null){
            model.addAttribute("userName", googleUser.getName());
        }

        return "index";
    }
}
