/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.collection.model;

import com.ssnagin.collectionmanager.collection.generators.RandomEnumGenerator;
import com.ssnagin.collectionmanager.description.annotations.Description;
import com.ssnagin.collectionmanager.object.Entity;
import com.ssnagin.collectionmanager.validation.annotations.NotEmpty;
import com.ssnagin.collectionmanager.validation.annotations.NotNull;
import com.ssnagin.collectionmanager.validation.annotations.PositiveNumber;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * Represents a music band with its properties and characteristics.
 * This class extends the Entity class and implements comparable interface for MusicBand objects.
 *
 * @author DEVELOPER
 * @version 1.0
 * @since 2023
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MusicBand extends Entity<MusicBand> {

    /**
     * Unique identifier for the music band.
     * Must be greater than 0 and is generated automatically.
     */
    @NotNull
    @PositiveNumber
    @Setter(AccessLevel.NONE)
    protected Long id;

    /**
     * Name of the music band.
     * Cannot be null or empty.
     */
    @NotNull
    @NotEmpty
    @Description(
            name = "name",
            description = "Name of the music band"
    )
    protected String name;

    /**
     * Coordinates associated with the music band.
     * Cannot be null.
     */
    @NotNull
    @Description(
            name = "coordinates",
            description = "Coordinates associated with the music band"
    )
    protected Coordinates coordinates;

    /**
     * Number of participants in the band.
     * Cannot be null and must be greater than 0.
     */
    @NotNull
    @PositiveNumber
    @Description(
            name = "number of participants",
            description = "Count of band members"
    )
    protected Long numberOfParticipants;

    /**
     * Number of singles released by the band.
     * Can be null, but must be greater than 0 if specified.
     */
    @PositiveNumber
    @Description(
            name = "singles count",
            description = "Number of singles released by the band"
    )
    protected Integer singlesCount;

    /**
     * Music genre of the band.
     * Can be null if not specified.
     */
    @Description(
            name = "music genre",
            description = "Genre of music performed by the band"
    )
    protected MusicGenre genre = null;

    /**
     * Best album of the band.
     * Cannot be null.
     */
    @NotNull
    @Description(
            name = "best album",
            description = "Best album released by the band"
    )
    protected Album bestAlbum;

    /**
     * Constructs a MusicBand with specified parameters.
     *
     * @param id                   the unique identifier for the band
     * @param name                 the name of the band
     * @param coordinates          the coordinates associated with the band
     * @param numberOfParticipants the number of band members
     * @param singlesCount         the number of singles released
     * @param genre                the music genre of the band
     * @param bestAlbum            the best album of the band
     * @throws IllegalArgumentException if any parameter violates validation constraints
     */
    public MusicBand(long id, String name, Coordinates coordinates, Long numberOfParticipants,
                     Integer singlesCount, MusicGenre genre, Album bestAlbum) {
        this.setId(id);
        this.setName(name);
        this.setCoordinates(coordinates);
        this.setNumberOfParticipants(numberOfParticipants);
        this.setSinglesCount(singlesCount);
        this.setGenre(genre);
        this.setBestAlbum(bestAlbum);
    }

    /**
     * Constructs a MusicBand with auto-generated ID.
     *
     * @param name                 the name of the band
     * @param coordinates          the coordinates associated with the band
     * @param numberOfParticipants the number of band members
     * @param singlesCount         the number of singles released
     * @param genre                the music genre of the band
     * @param bestAlbum            the best album of the band
     */
    public MusicBand(String name, Coordinates coordinates, Long numberOfParticipants,
                     Integer singlesCount, MusicGenre genre, Album bestAlbum) {
        this(
                MusicBand.generateId(),
                name,
                coordinates,
                numberOfParticipants,
                singlesCount,
                genre,
                bestAlbum
        );
    }

    /**
     * Default constructor for prepared statements.
     * Initializes all fields to null.
     */
    public MusicBand() {
        this(
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Generates a unique ID for the MusicBand.
     *
     * @return a positive long value representing the unique ID
     */
    private static long generateId() {
        long result = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        if (result == 0) result = MusicBand.generateId();
        return result;
    }

    /**
     * Sets the ID of the music band.
     *
     * @param id the unique identifier to set
     * @throws IllegalArgumentException if the id is not positive
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the MusicBand.
     *
     * @return a formatted string containing all band information
     */
    @Override
    public String toString() {
        return "MusicBand={" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", numberOfParticipants=" + numberOfParticipants +
                ", singlesCount=" + singlesCount +
                ", genre=" + genre +
                ", bestAlbum=" + bestAlbum +
                '}';
    }

    /**
     * Compares this MusicBand to another MusicBand for ordering.
     *
     * @param otherMusicBand the MusicBand to be compared
     * @return a negative integer, zero, or a positive integer as this MusicBand
     * is less than, equal to, or greater than the specified MusicBand
     */
    @Override
    public int compareTo(MusicBand otherMusicBand) {
        if (otherMusicBand == null) return 1;

        int result = this.getName().compareTo(otherMusicBand.getName());

        if (result == 0) result = this.getSinglesCount().compareTo(otherMusicBand.getSinglesCount());

        if (result == 0) result = this.getCoordinates().compareTo(otherMusicBand.getCoordinates());

        if (result == 0) result = this.getBestAlbum().compareTo(otherMusicBand.getBestAlbum());

        if (result == 0) result = this.getGenre().compareTo(otherMusicBand.getGenre());

        if (result == 0) result = this.getId().compareTo(otherMusicBand.getId());

        return result;
    }

    /**
     * Generates a MusicBand with random values for all fields.
     *
     * @return this MusicBand instance with randomized values
     */
    public MusicBand random() {
        this.setBestAlbum(new Album().random());
        this.setCoordinates(new Coordinates().random());
        this.setGenre((MusicGenre) new RandomEnumGenerator(MusicGenre.class).random());
        this.setName(RandomStringUtils.random(RANDOM_STRING_LENGTH, true, true));
        this.setSinglesCount(random.nextInt(0, Integer.MAX_VALUE));
        this.setNumberOfParticipants(random.nextLong(0, Long.MAX_VALUE));

        return this;
    }
}