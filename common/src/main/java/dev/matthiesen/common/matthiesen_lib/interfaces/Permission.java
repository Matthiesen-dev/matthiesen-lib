package dev.matthiesen.common.matthiesen_lib.interfaces;

import dev.matthiesen.common.matthiesen_lib.permission.PermissionLevel;
import net.minecraft.resources.ResourceLocation;

/**
 * Interface representing a permission that can be checked against a player's permissions. This is used for command permissions and other permission checks throughout the mod.
 * The Permission interface provides a standardized way to define permissions that can be checked against a player's permissions. Each Permission has a unique identifier,
 * a literal string representation, and a permission level. The identifier is used for registration and lookup, while the literal string is used for checking against a
 * player's permissions.
 *
 * <p>
 *     Based on Cobblemon's Permission system
 * </p>
 */
public interface Permission {

    /**
     * @return The unique identifier for this permission, which is used for registration and lookup.
     */
    ResourceLocation getIdentifier();

    /**
     * @return The literal string representation of this permission, which is used for checking against a player's permissions.
     */
    String getLiteral();

    /**
     * @return The level of this permission, which determines how it is checked against a player's permissions.
     */
    PermissionLevel getLevel();
}
