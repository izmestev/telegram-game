package ru.kosstantine.stainless.steel.rat.bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Button {

    @Id
    private Long id;

    private String text;

    private String smile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "button_redirect")
    @JoinColumn(name = "button_id")
    private Set<Long> redirect;

}
