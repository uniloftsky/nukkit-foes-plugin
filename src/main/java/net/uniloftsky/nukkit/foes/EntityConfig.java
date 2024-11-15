package net.uniloftsky.nukkit.foes;

/**
 * EntityConfig utility class.
 * <p>
 * {@link cn.nukkit.entity.Entity} class has an internal storage that holds the entity configuration.
 * IDs of records in this storage are stored as a simple {@link String}.
 * This EntityConfig class converts simple strings into fully objects that makes the development more safely.
 * <p>
 * IMPORTANT NOTE: This class could be changed if the API of {@link cn.nukkit.Nukkit} is changed.
 * <p>
 * TODO: every value must have the explanation comment that describes what exactly the value responsible for
 * TODO: for now most of the values are under research, so explanation comments will be gradually
 */
public enum EntityConfig {

    /**
     * TODO: Under research
     */
    ACTIVE_EFFECTS("ActiveEffects"),

    /**
     * TODO: Under research
     */
    AMPLIFIER("Amplifier"),

    /**
     * TODO: Under research
     */
    DURATION("Duration"),

    /**
     * TODO: Under research
     */
    SHOW_PARTICLES("ShowParticles"),

    /**
     * Value is responsible for the name above the entity.
     * Must be defined as string with a value that represents a custom name.
     *
     * <p>
     * Default value: <code>empty</code>.
     */
    CUSTOM_NAME("CustomName"),

    /**
     * TODO: Under research
     */
    CUSTOM_NAME_VISIBLE("CustomNameVisible"),

    /**
     * Value is responsible for the permanent display of the entity custom name if such exists.
     * <p>
     * By default, custom entity name is displayed only if a player looking at it.
     * <p>
     * Default value: <code>false</code>.
     */
    CUSTOM_NAME_ALWAYS_VISIBLE("CustomNameAlwaysVisible"),

    /**
     * TODO: Under research
     */
    POSITION("Pos"),

    /**
     * TODO: Under research
     */
    ROTATION("Rotation"),

    /**
     * TODO: Under research
     */
    MOTION("Motion"),

    /**
     * TODO: Under research
     */
    FALL_DISTANCE("FallDistance"),

    /**
     * TODO: Under research
     */
    FIRE("Fire"),

    /**
     * TODO: Under research
     */
    AIR("Air"),

    /**
     * TODO: Under research
     */
    ON_GROUND("OnGround"),

    /**
     * TODO: Under research
     */
    INVULNERABLE("Invulnerable"),

    /**
     * Value is responsible for the physical size of the entity.
     * Default value is a float of <code>1.0f</code>.
     */
    SCALE("Scale");

    /**
     * Technical, raw name that is used by {@link cn.nukkit.entity.Entity} class
     */
    public final String value;

    EntityConfig(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "technicalName='" + value + '\'' +
                '}';
    }
}
