package org.acme.model;

import org.acme.model.type.Role;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE")
    private Role role;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "XP", nullable = false, columnDefinition = "int default 0")
    private int xp;

    @Column(name = "LEVEL", nullable = false, columnDefinition = "int default 1")
    private int level;

    @Column(name = "MONEY", nullable = false, columnDefinition = "int default 0")
    private int money;

    @Column(name = "SKIN_MONEY", nullable = false, columnDefinition = "int default 0")
    private int skinMoney;

    @Column(name = "SECURE", nullable = false, columnDefinition = "Boolean default false")
    private Boolean secure = false;

    @Column(name = "PITY_6", nullable = false, columnDefinition = "int default 0")
    private int pity6;

    @Column(name = "PITY_5", nullable = false, columnDefinition = "int default 0")
    private int pity5;

    @Column(name = "PITY_4", nullable = false, columnDefinition = "int default 0")
    private int pity4;

    @Column(name = "ISDAILYDONE", nullable = false, columnDefinition = "Boolean default false")
    private Boolean isDailyDone = false;

    @Column(name = "ISHOURLYDONE", nullable = false, columnDefinition = "Boolean default false")
    private Boolean isHourlyDone = false;
}
