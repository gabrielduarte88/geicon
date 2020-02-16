package com.geicon.blue.api.models;

import com.geicon.blue.framework.persistence.models.GenericEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Log
 *
 * @author Gabriel Duarte
 */
@javax.persistence.Entity
public class Log extends GenericEntity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Usuário
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;
    /**
     * Registro
     */
    @Column(name = "registro", nullable = false, length = 255)
    private String registro;
    /**
     * Data de cadastro
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", nullable = false)
    private Date data;

    /**
     * Construtor
     */
    public Log() {
    }

    /**
     * Obtém o usuário
     *
     * @return Usuário
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Altera o usuário
     *
     * @param usuario Usuário
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtém o registro
     *
     * @return Registro
     */
    public String getRegistro() {
        return registro;
    }

    /**
     * Altera o registro
     *
     * @param registro Registro
     */
    public void setRegistro(String registro) {
        this.registro = registro;
    }

    /**
     * Obtém a data de cadastro
     *
     * @return Data de cadastro
     */
    public Date getData() {
        return data;
    }

    /**
     * Altera a data de cadastro
     *
     * @param data Data de cadastro
     */
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringBuilder("Log{").append("id=").append(id).append(", usuario=").append(usuario).append(", registro=").append(registro).append(", data=").append(data).append('}').toString();
    }

}
