package com.example.crud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "field")
public class FieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // string, number, date

    @Column(nullable = false)
    private boolean filterable = false;

    @Column(nullable = false)
    private boolean currentOnList = false;

    @Column(name = "polish_translation")
    private String polishTranslation;

    @Column(name = "dictionary_placeholder", length = 20000)
    private String dictionaryPlaceholder;

    @Column(name = "order_on_list", nullable = false)
    private Integer orderOnList = 0;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private ComponentEntity component;

    public FieldEntity() {}

    public FieldEntity(String uuid, String name, String type, boolean filterable, boolean currentOnList, String polishTranslation, String dictionaryPlaceholder, Integer orderOnList, Integer minLength, Integer maxLength, ComponentEntity component) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.filterable = filterable;
        this.currentOnList = currentOnList;
        this.polishTranslation = polishTranslation;
        this.dictionaryPlaceholder = dictionaryPlaceholder;
        this.orderOnList = orderOnList;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.component = component;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public boolean isCurrentOnList() {
        return currentOnList;
    }

    public void setCurrentOnList(boolean currentOnList) {
        this.currentOnList = currentOnList;
    }

    public String getPolishTranslation() {
        return polishTranslation;
    }

    public void setPolishTranslation(String polishTranslation) {
        this.polishTranslation = polishTranslation;
    }

    public String getDictionaryPlaceholder() {
        return dictionaryPlaceholder;
    }

    public void setDictionaryPlaceholder(String dictionaryPlaceholder) {
        this.dictionaryPlaceholder = dictionaryPlaceholder;
    }

    public Integer getOrderOnList() {
        return orderOnList;
    }

    public void setOrderOnList(Integer orderOnList) {
        this.orderOnList = orderOnList;
    }

    public ComponentEntity getComponent() {
        return component;
    }

    public void setComponent(ComponentEntity component) {
        this.component = component;
    }
}
