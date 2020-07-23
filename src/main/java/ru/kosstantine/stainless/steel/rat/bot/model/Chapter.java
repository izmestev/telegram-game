package ru.kosstantine.stainless.steel.rat.bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Chapter {

    @Id
    private Long id;

    @Lob
    private String text;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.EAGER)
    private Set<Button> buttons;

}
