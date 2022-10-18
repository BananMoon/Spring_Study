package jpabook.jpashop;

import java.lang.reflect.Method;

public class Parent {
    public Parent(String str, int cnt) {
        this.str = str;
        this.cnt = cnt;
    }

    public String str;
    public int cnt;


    public static void main(String[] args) {
        Child child = new Child("Parent1" , 1 , "Child1" , 1000);
        Parent parentTest = child;

        //parentTest.childStr = 업캐스팅 시 자식의 멤버에 접근불가 본인 (ParentTest)에만 접근가능

        //entityManager의 매개변수는 Object임
        ExtendTest extendTest = new ExtendTest();
        extendTest.persist(parentTest);

        //만약 내부 로직이 이렇게 형변환을 진행한다고 가정하면
//        Child convertChild = (Child) testObject;
//        System.out.println(convertChild.childCnt);
//        System.out.println(convertChild.childStr);

    }

}

class Child extends Parent{

    public Child(String str, int cnt , String childStr , int childCnt){
        super(str,cnt);
        this.childStr = childStr;
        this.childCnt = childCnt;
    }

    public String childStr;
    public int childCnt;

    public String getChildStr() {
        return childStr;
    }

    public int getChildCnt() {
        return childCnt;
    }
}

class ExtendTest{

    public void persist(Object obj){
        Class testClass = null;

        try{
            // 리플렉션 활용 Class 초기화
            testClass = Class.forName(obj.getClass().getName());
            //obj 클래스 캐스팅
            testClass.cast(obj);
            //해당 클래스의 메서드들 전부 가져오기
            Method[] methods = testClass.getDeclaredMethods();
            //반복
            for (Method o : methods) {
                //getXXX로 시작한다면
                if(o.getName().startsWith("get")){
                    //메서드 실행
                    System.out.println(o.invoke(obj));
                }
            }

        }catch (Exception e){

        }


    }

}