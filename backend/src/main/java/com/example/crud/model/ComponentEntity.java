package com.example.crud.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "component")
public class ComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean exportCsv = false;

    @Column(nullable = false)
    private boolean editable = true;

    @Column(nullable = false)
    private boolean copyable = false;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldEntity> fields = new ArrayList<>();

    public ComponentEntity() {}

    public ComponentEntity(String uuid, String name, boolean exportCsv, boolean editable, boolean copyable) {
        this.uuid = uuid;
        this.name = name;
        this.exportCsv = exportCsv;
        this.editable = editable;
        this.copyable = copyable;
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

    public boolean isExportCsv() {
        return exportCsv;
    }

    public void setExportCsv(boolean exportCsv) {
        this.exportCsv = exportCsv;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isCopyable() {
        return copyable;
    }

    public void setCopyable(boolean copyable) {
        this.copyable = copyable;
    }

    public List<FieldEntity> getFields() {
        return fields;
    }

    public void setFields(List<FieldEntity> fields) {
        this.fields = fields;
    }
}
