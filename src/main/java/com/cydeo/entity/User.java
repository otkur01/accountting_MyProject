package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private   String username; // must be unique
    private  String password;
    private  String firstname;
    private  String lastname;
    private  String phone;
    private  boolean enabled;
    @ManyToOne
    private Role role;
   @ManyToOne
    private Company company;

}
