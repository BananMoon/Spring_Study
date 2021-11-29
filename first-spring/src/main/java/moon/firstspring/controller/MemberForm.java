package moon.firstspring.controller;

public class MemberForm {
    private String name;
    // createMemberForm.html에서 form의 input에 name="name"과 MemberForm의 name과 연결되서 여기에 input을 넣어줌.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
