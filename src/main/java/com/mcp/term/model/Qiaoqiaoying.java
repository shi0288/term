package com.mcp.term.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "qiaoqiaoying")
public class Qiaoqiaoying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String token;

    private String loginUrl;

    private String dataUrl;

    private String prizeUrl;

    private String dataUrlFfc;

    private String prizeUrlFfc;

    private String refreshToken;

    private Date createAt;

    private Date expiresIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getPrizeUrl() {
        return prizeUrl;
    }

    public void setPrizeUrl(String prizeUrl) {
        this.prizeUrl = prizeUrl;
    }

    public String getDataUrlFfc() {
        return dataUrlFfc;
    }

    public void setDataUrlFfc(String dataUrlFfc) {
        this.dataUrlFfc = dataUrlFfc;
    }

    public String getPrizeUrlFfc() {
        return prizeUrlFfc;
    }

    public void setPrizeUrlFfc(String prizeUrlFfc) {
        this.prizeUrlFfc = prizeUrlFfc;
    }
}
