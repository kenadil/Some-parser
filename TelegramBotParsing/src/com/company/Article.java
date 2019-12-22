package com.company;

public class Article
{
    private String url;
    private String name;

    public Article(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return
         "Article(" + "url = '" + url + '\''  +
                 ", name = '" + name + '\'' + '}';}

}
