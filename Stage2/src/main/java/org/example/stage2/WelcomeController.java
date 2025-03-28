package org.example.stage2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    @ResponseBody
    @RequestMapping("/greet")
    public String greet() {
        return "welcome to the world of spring boot";
    }
}
