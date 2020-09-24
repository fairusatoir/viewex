package com.viewex.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log_pdf")
public class logModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "file_name")
    private String file_name;

    @Basic
    @Column(name = "printed_date")
    private Date printed_date;

    @Basic
    @Column(name = "user_id")
    private String user_id;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "content")
    private String content;

    public logModel(String file_name, Date printed_date, String user_id, String status, String content) {
        this.file_name = file_name;
        this.printed_date = printed_date;
        this.user_id = user_id;
        this.status = status;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Date getPrinted_date() {
        return printed_date;
    }

    public void setPrinted_date(Date printed_date) {
        this.printed_date = printed_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
