package br.com.cardapio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "METALOCAL")
public class MetaLocal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metalocal", unique = true, nullable = false)
    private Integer id;

    @Column(name = "tp_metalocal")
    public String tipoMetaLocal;

    @Column(name = "ds_metalocal_singular")
    public String metaLocalSingular;

    @Column(name = "ds_metalocal_plural")
    public String metaLocalPlural;

    @Column(name = "ds_lingua")
    public String lingua;
    
    @Transient
    public static final String MESA = "MESA";
    
    @Transient
    public static final String QUARTO = "QUARTO";

    public String getTipoMetaLocal() {
        return tipoMetaLocal;
    }

    public void setTipoMetaLocal(String tipoMetaLocal) {
        this.tipoMetaLocal = tipoMetaLocal;
    }

    public String getMetaLocalSingular() {
        return metaLocalSingular;
    }

    public void setMetaLocalSingular(String metaLocalSingular) {
        this.metaLocalSingular = metaLocalSingular;
    }

    public String getMetaLocalPlural() {
        return metaLocalPlural;
    }

    public void setMetaLocalPlural(String metaLocalPlural) {
        this.metaLocalPlural = metaLocalPlural;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
