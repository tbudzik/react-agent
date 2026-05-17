package com.example.crud.dto;

import java.util.List;

public class ComponentDTO {
    private String id;
    private String name;
    private boolean exportCsv;
    private boolean editable;
    private boolean copyable;
    private List<FieldDTO> fields;

    public ComponentDTO() {}

    public ComponentDTO(String id, String name, boolean exportCsv, boolean editable, boolean copyable, List<FieldDTO> fields) {
        this.id = id;
        this.name = name;
        this.exportCsv = exportCsv;
        this.editable = editable;
        this.copyable = copyable;
        this.fields = fields;
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

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }
}
