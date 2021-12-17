package moonz.core.singleton;

public class SingletonService {
    private static final SingletonService instance = new SingletonService();    // 자기 자신을 내부 private static 영역에 딱 하나만 생성되어 참조

    // static 메서드인 getInstance 통해서만 조회하도록 허용
    public static SingletonService getInstance() {
        return instance;
    }

    // private 생성자 -> 외부에서 생성하지 못하도록 한다.
    private SingletonService() {}

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
