package com.geicon.blue.framework.persistence.models;

import com.geicon.blue.framework.util.Security;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Implementação generica de Entity
 *
 * @author Gabriel Duarte
 */
@MappedSuperclass
public abstract class GenericEntity implements Entity {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * ID da entidade
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    protected Integer id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return this.id != null ? Security.encrypt(this.id.toString()) : null;
    }

    @Override
    public void setCode(String code) {
        if (code == null || code.isEmpty()) {
            this.id = null;
        }
        else {
            this.id = Integer.parseInt(Security.decrypt(code));
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        final GenericEntity other = (GenericEntity) obj;

        return Objects.equals(this.id, other.getId()) || (this.id != null && this.id.equals(other.getId()));
    }
}
