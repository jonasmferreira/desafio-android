package br.com.jonasmendes.desafio_android.domain;

public class User extends Owner {
    public User(String login, String avatar_url, String name) {
        super(login, avatar_url, name);
    }
}