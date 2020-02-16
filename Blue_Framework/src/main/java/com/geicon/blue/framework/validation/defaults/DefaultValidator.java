package com.geicon.blue.framework.validation.defaults;

import com.geicon.blue.framework.validation.ValidationItem;
import com.geicon.blue.framework.validation.Validator;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Implementação padrão de Validator
 *
 * @author Gabriel Duarte
 */
public class DefaultValidator implements Validator {
    /**
     * Itens a serem verificados
     */
    private final Set<ValidationItem> items;

    /**
     * Construtor
     */
    public DefaultValidator() {
        items = new LinkedHashSet<>(0);
    }

    /**
     * Construtor
     *
     * @param items Itens a serem verificados
     */
    public DefaultValidator(ValidationItem... items) {
        this.items = new LinkedHashSet<>(Arrays.asList(items));
    }

    @Override
    public void add(ValidationItem item) {
        items.add(item);
    }

    @Override
    public void add(Boolean valid, String message) {
        items.add(new DefaultValidationItem(valid, message));
    }

    @Override
    public void add(String value, String message) {
        items.add(new DefaultValidationItem(value, message));
    }

    @Override
    public void add(Object value, String message) {
        items.add(new DefaultValidationItem(value, message));
    }

    @Override
    public Set<ValidationItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    @Override
    public void merge(Validator validator) {
        items.addAll(validator.getItems());
    }

    @Override
    public boolean hasErrors() {
        return items.stream().anyMatch((item) -> (!item.isValid()));
    }

    @Override
    public Set<String> getErrors() {
        Set<String> errors = new LinkedHashSet<>(0);

        items.stream().filter((item) -> (!item.isValid())).forEach((item) -> {
            errors.add(item.getMessage());
        });

        return errors;
    }
}
