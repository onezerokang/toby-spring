package kang.onezero.tobyspring.user.domain;

public class User {
    String id;
    String name;
    String password;

    // 자바빈 규약을 따르는 클래스에 생성자를 명시적으로 추가할 때는 디폴트 생성자를 정의해주는 것을 잊지 말자.
    public User() {
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
