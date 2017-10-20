package com.github.restart1025.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class BaseEntity implements Serializable {

    @Id
    private Integer id;
    @Column(name="deleted")
    private boolean deleted;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
