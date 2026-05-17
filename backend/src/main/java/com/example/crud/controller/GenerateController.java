package com.example.crud.controller;

import com.example.crud.model.ComponentEntity;
import com.example.crud.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@CrossOrigin(origins = "*")
public class GenerateController {

    @Autowired
    private ComponentRepository componentRepository;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateComponentFile(@RequestParam String componentId) {
        System.out.println("Generating file for component ID: " + componentId);
        return componentRepository.findByUuid(componentId)
                .map(this::buildFileResponse)
                .orElse(ResponseEntity.notFound().build());
    }

    private ResponseEntity<byte[]> buildFileResponse(ComponentEntity component) {
        StringBuilder builder = new StringBuilder();
        builder.append("Nazwa komponentu: ").append(component.getName()).append("\n");
        builder.append("Eksport do CSV: ").append(component.isExportCsv() ? "Tak" : "Nie").append("\n");
        builder.append("Możliwość edycji: ").append(component.isEditable() ? "Tak" : "Nie").append("\n");
        builder.append("Możliwość kopiowania: ").append(component.isCopyable() ? "Tak" : "Nie").append("\n\n");
        builder.append("Pola:\n");

        component.getFields().forEach(field -> {
            builder.append("- Nazwa: ").append(field.getName()).append("\n");
            builder.append("  Typ: ").append(field.getType()).append("\n");
            builder.append("  Filtr: ").append(field.isFilterable() ? "Tak" : "Nie").append("\n");
            builder.append("  Min dł.: ").append(field.getMinLength() != null ? field.getMinLength() : "-").append("\n");
            builder.append("  Max dł.: ").append(field.getMaxLength() != null ? field.getMaxLength() : "-").append("\n\n");
        });

        String fileName = component.getName().replaceAll("[^a-zA-Z0-9_-]", "_") + ".txt";
        byte[] content = builder.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }
}
