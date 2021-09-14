package moon.firstspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// controller annotation 필수
@Controller
public class HelloController {
    //web application에서 /hello 호출 시, 해당 메서드 호출

    @GetMapping("hello")
    public String hello(Model model) {
        //mvc 중 m
        model.addAttribute("data", "hello!!");   //value인 hello
        return "hello";
    }

    @GetMapping("hello-mvc")
    //웹사이트의 url로 파라미터를 받아보자. model에 담아서 View에서 렌더링할 때 사용
    //값을 넘기는것을 선택으로 하고 싶으면
    //public STring helloMvc(@RequestParam(value="name" required=true) String name, Model model)
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    //중요!
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;     //요청한 클라이언트에 hello spring 이 그대로 넘어감.
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    //꿀팁. Ctrl+Shift+Enter -> (); 문장 완성시켜줌
    //HelloController.Hello  이런 식으로 사용 가능
    static class Hello {
        private String name;
        // private이니까 객체를 외부에서 접근 못하므로 getter&setter 메서드 이용
// property 접근 방식 : getter and setter : Ctrl+N-> action -> Getter and Setter 클릭
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
