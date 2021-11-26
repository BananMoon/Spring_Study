package moon.firstspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("data", name);
        return "hello-template";
    }

    @GetMapping("hello-string") 
    @ResponseBody   // html의 body 태그가 아니라, http의 응답 body부분에 "Hello" +name 을 바로 전달한다는 의미.
    public String helloString(@RequestParam("name") String name) {
        return "Hello " + name; // 문자를 그대로 내려줌.
    }

    @GetMapping("hello-api")
    @ResponseBody
    public  Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName("moon");
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
                    return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }
}