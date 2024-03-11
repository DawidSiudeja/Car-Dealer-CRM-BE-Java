package com.example.cardealer.entity.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "dealer")
@Entity
public class Dealer {

    @Id
    @GeneratedValue(generator = "dealer_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "dealer_id_seq",sequenceName = "dealer_id_seq",allocationSize = 1)
    private long id;
    private String uuid;
    private String logoPath;
    private String companyName;
    private String telephone1;
    private String telephone2;
    private String telephone3;
    private String mail1;
    private String mail2;
    private String mail3;
    private String nip;
    private String facebookLink;
    private String instagramLink;
    private String tiktokLink;
    private String otomotoLink;
    private String address;
    private String descHtml;

}
