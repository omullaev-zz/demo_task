package com.app;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "urls", indexes = {@Index(name = "URL_INDX", columnList = "rawValue")},
        uniqueConstraints = {@UniqueConstraint(name = "URL_UNQ", columnNames = "rawValue")})
@EntityListeners(AuditingEntityListener.class)
public class UrlForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 300, message = "must be less than or equal to 300")
    @Pattern(regexp = ".+://.+\\..+",
            message = "must match absolute-URI mask = scheme \":\" hier-part [ \"?\" query ]")
    private String rawValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }
}
