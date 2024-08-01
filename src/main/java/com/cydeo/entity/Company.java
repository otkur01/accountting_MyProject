package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company extends BaseEntity {

    private   String title;
    private   String phone;
    private   String website;
    @Enumerated(EnumType.STRING)
    private   CompanyStatus companyStatus ;
    @OneToOne(cascade = CascadeType.ALL)
    private   Address address;
}
