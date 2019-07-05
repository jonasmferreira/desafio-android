package br.com.jonasmendes.desafio_android.domain;

public class Owner {
    String login;
    String avatar_url;
    String name;

    public Owner(String login, String avatar_url, String name) {
        this.login = login;
        this.avatar_url = avatar_url;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "login='" + login + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
