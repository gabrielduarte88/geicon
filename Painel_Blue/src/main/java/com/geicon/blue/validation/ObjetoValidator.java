package com.geicon.blue.validation;

import com.geicon.blue.api.models.Objeto;
import com.geicon.blue.api.models.ObjetoOcorrencia;
import com.geicon.blue.api.models.Relacao;
import com.geicon.blue.framework.validation.defaults.DefaultValidator;

/**
 * Validação dos objetos
 *
 * @author Gabriel Duarte
 */
public class ObjetoValidator extends DefaultValidator {
    /**
     * Construtor
     *
     * @param relacao Relação
     * @param objeto Objeto
     * @param ocorrencia Ocorrência
     * @param posicao Posição
     */
    public ObjetoValidator(Relacao relacao, Objeto objeto, ObjetoOcorrencia ocorrencia, String posicao) {
        if (relacao != null && objeto != null && ocorrencia != null && posicao != null) {
            add(objeto.getNome(), "O nome do objeto não foi informado;");

//            add(ocorrencia.getDocumento(), "O documento da ocorrência não foi informado;");
//            add(ocorrencia.getValor(), "O valor da ocorrência não foi informado;");
//            add(ocorrencia.getPosicaoInicial(), "A posição inicial da ocorrência não foi informada;");
//            add(ocorrencia.getPosicaoFinal(), "A posição final da ocorrência não foi informada;");
        }
        else {
            add(relacao, "Os dados necessários não foram enviados");
            add(objeto, "Os dados necessários não foram enviados");
            add(ocorrencia, "Os dados necessários não foram enviados");
            add(posicao, "Os dados necessários não foram enviados");
        }
    }
}
