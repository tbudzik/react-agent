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

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            builder.append("  Tłumaczenie polskie: ").append(field.getPolishTranslation() != null ? field.getPolishTranslation() : "-").append("\n");
            builder.append("  Filtr: ").append(field.isFilterable() ? "Tak" : "Nie").append("\n");
            builder.append("  Obecny na liście: ").append(field.isCurrentOnList() ? "Tak" : "Nie").append("\n");
            builder.append("  Min dł.: ").append(field.getMinLength() != null ? field.getMinLength() : "-").append("\n");
            builder.append("  Max dł.: ").append(field.getMaxLength() != null ? field.getMaxLength() : "-").append("\n\n");
        });

        StringBuilder filtersBuilder = new StringBuilder();
        component.getFields().stream()
                .filter(field -> field.isFilterable())
                .forEach(field -> filtersBuilder.append(field.getName()).append("\n"));

        StringBuilder listBuilder = new StringBuilder();
        component.getFields().stream()
                .filter(field -> field.isCurrentOnList())
                .sorted(Comparator.comparingInt(field -> field.getOrderOnList() != null ? field.getOrderOnList() : 0))
                .forEach(field -> listBuilder.append(field.getName()).append("\n"));

        String textFileName = component.getName().replaceAll("[^a-zA-Z0-9_-]", "_") + ".txt";
        String filtersFileName = "filtry.txt";
        String listFileName = "Lista.txt";
        String zipFileName = component.getName().replaceAll("[^a-zA-Z0-9_-]", "_") + ".zip";
        byte[] content = builder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] filtersContent = filtersBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] listContent = listBuilder.toString().getBytes(StandardCharsets.UTF_8);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry(textFileName);
            zos.putNextEntry(entry);
            zos.write(content);
            zos.closeEntry();

            ZipEntry filtersEntry = new ZipEntry(filtersFileName);
            zos.putNextEntry(filtersEntry);
            zos.write(filtersContent);
            zos.closeEntry();

            ZipEntry listEntry = new ZipEntry(listFileName);
            zos.putNextEntry(listEntry);
            zos.write(listContent);
            zos.closeEntry();

            zos.finish();
            byte[] zipBytes = baos.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
