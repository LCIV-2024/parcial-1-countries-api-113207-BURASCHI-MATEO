package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
