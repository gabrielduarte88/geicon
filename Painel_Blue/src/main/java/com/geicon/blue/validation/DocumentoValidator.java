package com.geicon.blue.validation;

import com.geicon.blue.api.models.Documento;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos documentos
 *
 * @author Gabriel Duarte
 */
public class DocumentoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param documento Documento validado
     */
    public DocumentoValidator(Documento documento) {
        if (documento != null) {
            add(documento.getNome(), "O campo 'nome' não foi preenchido;");
            add(documento.getTexto(), "O campo 'texto' não foi preenchido;");
        }
        else {
            add(documento, "Os dados necessários não foram enviados");
        }
    }
}
