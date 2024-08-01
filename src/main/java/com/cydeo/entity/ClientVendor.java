package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.ClientVendorType;
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
@Table(name = "clients_vendors")
public class ClientVendor extends BaseEntity {

  private   String clientVendorName;
    private  String phone;
    private  String website;
  @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @ManyToOne
    private  Company company ;

}
