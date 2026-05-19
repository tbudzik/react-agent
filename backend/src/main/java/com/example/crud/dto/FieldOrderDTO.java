package com.example.crud.dto;

public class FieldOrderDTO {
    private String id;
    private Integer orderOnList;

    public FieldOrderDTO() {}

    public FieldOrderDTO(String id, Integer orderOnList) {
        this.id = id;
        this.orderOnList = orderOnList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrderOnList() {
        return orderOnList;
    }

    public void setOrderOnList(Integer orderOnList) {
        this.orderOnList = orderOnList;
    }
}
