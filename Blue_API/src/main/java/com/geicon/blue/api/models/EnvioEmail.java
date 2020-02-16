package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EnvioEmail
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
@Table(name = "EnvioEmail", catalog = "dbKMS")
public class EnvioEmail extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * E-mail destino
     */
    @Column(name = "emailDestino", columnDefinition = "TEXT")
    private String emailDestino;
    /**
     * E-mail destino CC
     */
    @Column(name = "emailDestinoCC", columnDefinition = "TEXT")
    private String emailDestinoCC;
    /**
     * E-mail destino CCO
     */
    @Column(name = "emailDestinoCCO", columnDefinition = "TEXT")
    private String emailDestinoCCO;
    /**
     * Assunto
     */
    @Column(name = "assunto", length = 255)
    private String assunto;
    /**
     * Texto
     */
    @Column(name = "texto", columnDefinition = "TEXT")
    private String texto;
    /**
     * Data de acesso
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", length = 19)
    private Date data;
    /**
     * Data de envio
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataEnvio", length = 19)
    private Date dataEnvio;

    /**
     * Construtor
     */
    public EnvioEmail() {
    }

    /**
     * Obtém o e-mail do destino
     *
     * @return E-mail do destino
     */
    public String getEmailDestino() {
        return emailDestino;
    }

    /**
     * Altera o e-mail do destino
     *
     * @param emailDestino E-mail do destino
     */
    public void setEmailDestino(String emailDestino) {
        this.emailDestino = emailDestino;
    }

    /**
     * Obtém o e-mail CC do destino
     *
     * @return E-mail CC do destino
     */
    public String getEmailDestinoCC() {
        return emailDestinoCC;
    }

    /**
     * Altera o e-mail CC do destino
     *
     * @param emailDestinoCC CC E-mail do destino
     */
    public void setEmailDestinoCC(String emailDestinoCC) {
        this.emailDestinoCC = emailDestinoCC;
    }

    /**
     * Obtém o e-mail CCO do destino
     *
     * @return E-mail CCO do destino
     */
    public String getEmailDestinoCCO() {
        return emailDestinoCCO;
    }

    /**
     * Altera o e-mail CCO do destino
     *
     * @param emailDestinoCCO CCO E-mail do destino
     */
    public void setEmailDestinoCCO(String emailDestinoCCO) {
        this.emailDestinoCCO = emailDestinoCCO;
    }

    /**
     * Obtém o assunto
     *
     * @return Assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * Altera o assunto
     *
     * @param assunto Assunto
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * Obtém o texto
     *
     * @return Texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Altera o texto
     *
     * @param texto Texto
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtém a data
     *
     * @return Data
     */
    public Date getData() {
        return data;
    }

    /**
     * Altera a data
     *
     * @param data Data
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Obtém a data de envio
     *
     * @return Data de envio
     */
    public Date getDataEnvio() {
        return dataEnvio;
    }

    /**
     * Altera a data de envio
     *
     * @param dataEnvio Data de envio
     */
    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Override
    public String toString() {
        return new StringBuilder("EnvioEmail{").append("id=").append(id).append(", emailDestino=").append(emailDestino).append(", emailDestinoCC=").append(emailDestinoCC).append(", emailDestinoCCO=").append(emailDestinoCCO).append(", assunto=").append(assunto).append(", texto=").append(texto).append(", data=").append(data).append(", dataEnvio=").append(dataEnvio).append('}').toString();
    }
}
