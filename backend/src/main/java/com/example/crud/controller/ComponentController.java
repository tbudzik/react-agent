package com.example.crud.controller;

import com.example.crud.model.ComponentEntity;
import com.example.crud.model.FieldEntity;
import com.example.crud.dto.ComponentDTO;
import com.example.crud.dto.FieldDTO;
import com.example.crud.repository.ComponentRepository;
import com.example.crud.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/components")
@CrossOrigin(origins = "*")
public class ComponentController {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private FieldRepository fieldRepository;

    private ComponentDTO entityToDTO(ComponentEntity entity) {
        List<FieldDTO> fieldDTOs = entity.getFields().stream()
                .map(f -> new FieldDTO(f.getUuid(), f.getName(), f.getType(), f.isFilterable(), f.getMinLength(), f.getMaxLength()))
                .collect(Collectors.toList());
        return new ComponentDTO(entity.getUuid(), entity.getName(), entity.isExportCsv(), entity.isEditable(), entity.isCopyable(), fieldDTOs);
    }

    @GetMapping
    public List<ComponentDTO> listComponents() {
        return componentRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentDTO> getComponent(@PathVariable String id) {
        return componentRepository.findByUuid(id)
                .map(this::entityToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponentDTO> createComponent(@RequestBody ComponentDTO dto) {
        String uuid = "component-" + System.currentTimeMillis();
        ComponentEntity entity = new ComponentEntity(uuid, dto.getName(), dto.isExportCsv(), dto.isEditable(), dto.isCopyable());
        ComponentEntity saved = componentRepository.save(entity);
        return ResponseEntity.ok(entityToDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentDTO> updateComponent(@PathVariable String id, @RequestBody ComponentDTO dto) {
        return componentRepository.findByUuid(id)
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setExportCsv(dto.isExportCsv());
                    entity.setEditable(dto.isEditable());
                    entity.setCopyable(dto.isCopyable());
                    ComponentEntity updated = componentRepository.save(entity);
                    return ResponseEntity.ok(entityToDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable String id) {
        return componentRepository.findByUuid(id)
                .map(entity -> {
                    componentRepository.delete(entity);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{componentId}/fields")
    public ResponseEntity<List<FieldDTO>> listFields(@PathVariable String componentId) {
        return componentRepository.findByUuid(componentId)
                .map(entity -> {
                    List<FieldDTO> fields = entity.getFields().stream()
                            .map(f -> new FieldDTO(f.getUuid(), f.getName(), f.getType(), f.isFilterable(), f.getMinLength(), f.getMaxLength()))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(fields);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{componentId}/fields")
    public ResponseEntity<FieldDTO> createField(@PathVariable String componentId, @RequestBody FieldDTO dto) {
        return componentRepository.findByUuid(componentId)
                .map(component -> {
                    String uuid = "field-" + System.currentTimeMillis();
                    FieldEntity field = new FieldEntity(uuid, dto.getName(), dto.getType(), dto.isFilterable(), dto.getMinLength(), dto.getMaxLength(), component);
                    FieldEntity saved = fieldRepository.save(field);
                    FieldDTO result = new FieldDTO(saved.getUuid(), saved.getName(), saved.getType(), saved.isFilterable(), saved.getMinLength(), saved.getMaxLength());
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{componentId}/fields/{fieldId}")
    public ResponseEntity<FieldDTO> updateField(@PathVariable String componentId, @PathVariable String fieldId, @RequestBody FieldDTO dto) {
        return componentRepository.findByUuid(componentId)
                .flatMap(component -> fieldRepository.findByUuid(fieldId))
                .map(field -> {
                    field.setName(dto.getName());
                    field.setType(dto.getType());
                    field.setFilterable(dto.isFilterable());
                    field.setMinLength(dto.getMinLength());
                    field.setMaxLength(dto.getMaxLength());
                    FieldEntity updated = fieldRepository.save(field);
                    FieldDTO result = new FieldDTO(updated.getUuid(), updated.getName(), updated.getType(), updated.isFilterable(), updated.getMinLength(), updated.getMaxLength());
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{componentId}/fields/{fieldId}")
    public ResponseEntity<Void> deleteField(@PathVariable String componentId, @PathVariable String fieldId) {
        return componentRepository.findByUuid(componentId)
                .flatMap(component -> fieldRepository.findByUuid(fieldId))
                .map(field -> {
                    fieldRepository.delete(field);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
