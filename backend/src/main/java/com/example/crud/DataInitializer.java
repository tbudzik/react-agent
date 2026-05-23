package com.example.crud;

import com.example.crud.model.ComponentEntity;
import com.example.crud.model.FieldEntity;
import com.example.crud.repository.ComponentRepository;
import com.example.crud.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public void run(String... args) throws Exception {
        if (componentRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Komponenty
        ComponentEntity component1 = new ComponentEntity(
                "component-1",
                "Formularz kontaktowy",
                true,
                true,
                false
        );
        component1 = componentRepository.save(component1);

        ComponentEntity component2 = new ComponentEntity(
                "component-2",
                "Rejestracja",
                false,
                true,
                true
        );
        component2 = componentRepository.save(component2);

        // Pola dla komponentu 1
        FieldEntity field1 = new FieldEntity(
                "field-1",
                "Imię",
                "string",
                true,
                true,
                "Imię",
                "Wybierz imię z listy...",
                1,
                2,
                50,
                component1
        );
        fieldRepository.save(field1);

        FieldEntity field2 = new FieldEntity(
                "field-2",
                "Email",
                "string",
                true,
                true,
                "E-mail",
                "Wybierz adres e-mail...",
                2,
                5,
                100,
                component1
        );
        fieldRepository.save(field2);

        // Pola dla komponentu 2
        FieldEntity field3 = new FieldEntity(
                "field-3",
                "Data urodzenia",
                "date",
                false,
                false,
                "Data urodzenia",
                "Wybierz datę...",
                0,
                null,
                null,
                component2
        );
        fieldRepository.save(field3);
    }
}
