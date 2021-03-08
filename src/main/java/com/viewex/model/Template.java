package com.viewex.model;

import javax.persistence.*;

@Entity
@Table(name = "to_pdf_template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "content_sum")
    private int content_sum;

    @Basic
    @Column(name = "template_name")
    private String template_name;

    @Basic
    @Column(name = "template_file")
    private String template_file;

    @Basic
    @Column(name = "format_content")
    private String format_content;

    @Basic
    @Column(name = "file_type")
    private String file_type;

    @Basic
    @Column(name = "image")
    private String image;

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getContent_sum() {
        return content_sum;
    }

    public void setContent_sum(int content_sum) {
        this.content_sum = content_sum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplate_file() {
        return template_file;
    }

    public void setTemplate_file(String template_file) {
        this.template_file = template_file;
    }

    public String getFormat_content() {
        return format_content;
    }

    public void setFormat_content(String format_content) {
        this.format_content = format_content;
    }
}
