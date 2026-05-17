package com.example.crud.dto;

import java.util.List;

public class FieldDTO {
    private String id;
    private String name;
    private String type;
    private boolean filterable;
    private Integer minLength;
    private Integer maxLength;

    public FieldDTO() {}

    public FieldDTO(String id, String name, String type, boolean filterable, Integer minLength, Integer maxLength) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.filterable = filterable;
        this.minLength = minLength;
        this.maxLength = maxLength;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
