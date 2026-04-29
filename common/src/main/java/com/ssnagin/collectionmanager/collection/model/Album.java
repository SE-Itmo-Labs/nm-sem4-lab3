/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.collection.model;

import com.ssnagin.collectionmanager.description.annotations.Description;
import com.ssnagin.collectionmanager.object.Entity;
import com.ssnagin.collectionmanager.validation.annotations.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author DEVELOPER
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Album extends Entity<Album> {
    @NotNull
    @Description(
            name = "имя",
            description = "какое-то описание"
    )
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Description(
            name = "количество треков",
            description = "какое-то описание"
    )
    private Long tracks = null; //Поле может быть null, Значение поля должно быть больше 0

    public Album(String name, Long tracks) {
        setName(name);
        setTracks(tracks);
    }

    public Album() {
        this(null, null);
    }

    @Override
    public String toString() {
        return "Album={name='" + getName() + "', tracks='" + Long.toString(getTracks()) + "'}";
    }

    @Override
    public int compareTo(Album otherAlbum) {
        int result = this.name.compareTo(otherAlbum.name);

        if (result == 0) result = this.tracks.compareTo(otherAlbum.tracks);

        return result;
    }

    public Album random() {
        this.setName(RandomStringUtils.random(RANDOM_STRING_LENGTH, true, true));
        this.setTracks(random.nextLong(0, Long.MAX_VALUE));
        return this;
    }
}
