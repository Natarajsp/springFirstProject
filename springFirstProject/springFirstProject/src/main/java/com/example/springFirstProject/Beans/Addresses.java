package com.example.springFirstProject.Beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // It will help to Ignore the Json Input while Binding the data if User is sending the UserId as Input.
    @Column(name = "addressId")
    private Long addressId;

    private String street;

    private String city;

    private String state;

    private String country;

    private String zipcode;

}
