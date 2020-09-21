package com.viewex.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "test")
public class Debitur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ktp")
    private Integer id;

    private Integer no_app;
    private Integer kredit_max;
    private Integer interest_percen;
    private Integer interest_rp;

    private String name;
    private Date birthday;
    private Date app_date;
    private Date kredit_start;
    private Date kredit_end;
    private Date date_sign;
    private String kredit_max_text;
    private String interest_rp_text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo_app() {
        return no_app;
    }

    public void setNo_app(Integer no_app) {
        this.no_app = no_app;
    }

    public Integer getKredit_max() {
        return kredit_max;
    }

    public void setKredit_max(Integer kredit_max) {
        this.kredit_max = kredit_max;
    }

    public Integer getInterest_percen() {
        return interest_percen;
    }

    public void setInterest_percen(Integer interest_percen) {
        this.interest_percen = interest_percen;
    }

    public Integer getInterest_rp() {
        return interest_rp;
    }

    public void setInterest_rp(Integer interest_rp) {
        this.interest_rp = interest_rp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getApp_date() {
        return app_date;
    }

    public void setApp_date(Date app_date) {
        this.app_date = app_date;
    }

    public Date getKredit_start() {
        return kredit_start;
    }

    public void setKredit_start(Date kredit_start) {
        this.kredit_start = kredit_start;
    }

    public Date getKredit_end() {
        return kredit_end;
    }

    public void setKredit_end(Date kredit_end) {
        this.kredit_end = kredit_end;
    }

    public Date getDate_sign() {
        return date_sign;
    }

    public void setDate_sign(Date date_sign) {
        this.date_sign = date_sign;
    }

    public String getKredit_max_text() {
        return kredit_max_text;
    }

    public void setKredit_max_text(String kredit_max_text) {
        this.kredit_max_text = kredit_max_text;
    }

    public String getInterest_rp_text() {
        return interest_rp_text;
    }

    public void setInterest_rp_text(String interest_rp_text) {
        this.interest_rp_text = interest_rp_text;
    }
}
