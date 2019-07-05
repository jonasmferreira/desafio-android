package br.com.jonasmendes.desafio_android.domain;

public class PullRequest {
    String title;
    String body;
    String created_at;
    String html_url;
    User user;

    public PullRequest(String title, String body, String created_at, String html_url, User user) {
        this.title = title;
        this.body = body;
        this.created_at = created_at;
        this.html_url = html_url;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", created_at='" + created_at + '\'' +
                ", user=" + user.toString() +
                '}';
    }
}
