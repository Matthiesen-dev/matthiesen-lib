package dev.matthiesen.common.matthiesen_lib.core.compat;

import java.lang.reflect.Method;

/**
 * A platform-agnostic builder for customizing Ember's Text API immersive messages.
 *
 * <p>This utility provides a fluent API to configure visual and textual properties of immersive messages
 * without directly importing platform-dependent Ember classes. Configuration is applied at send-time
 * via reflective method invocation on the underlying {@code ImmersiveMessage} instance.
 *
 * <p><b>Usage Example:</b>
 * <pre>{@code
 * // Get the parser and obtain the Embers compatibility layer
 * MatthiesenLibTextParser parser = MatthiesenLib.getParser(MatthiesenLibBuiltInTextParsers.EMBER);
 * MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
 *
 * if (compat != null) {
 *     // Send a simple message at the top of the screen
 *     compat.sendMessage(player, "Hello World", 100f);
 *
 *     // Send a message with customization
 *     MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create()
 *         .anchor(TextAnchor.TOP_CENTER)
 *         .align(TextAlign.CENTER)
 *         .scale(1.5f)
 *         .shadow(true)
 *         .fadeInTicks(10)
 *         .fadeOutTicks(20);
 *
 *     compat.sendMessage(player, "Customized Message", 100f, builder);
 *
 *     // Send a typewriter effect message
 *     MatthiesenLibImmersiveMessageBuilder typewriterBuilder = MatthiesenLibImmersiveMessageBuilder.create()
 *         .typewriter(0.5f)
 *         .offset(0f, 100f)
 *         .anchor(TextAnchor.MIDDLE);
 *
 *     compat.sendMessage(player, "Typing out letter by letter...", 200f, typewriterBuilder);
 * }
 * }</pre>
 *
 * <p>All properties have sensible defaults matching Ember's defaults. Only explicitly-set properties
 * are applied to the message; others retain Ember's defaults.
 */
@SuppressWarnings("unused")
public class MatthiesenLibImmersiveMessageBuilder {
	/**
	 * Screen anchor positions for immersive messages.
	 * Defines a 3×3 grid of horizontal and vertical anchoring points.
	 */
	public enum TextAnchor {
		/** Top-left corner of the screen */
		TOP_LEFT,
		/** Top-center of the screen */
		TOP_CENTER,
		/** Top-right corner of the screen */
		TOP_RIGHT,
		/** Middle-left edge of the screen */
		MIDDLE_LEFT,
		/** Center of the screen */
		MIDDLE,
		/** Middle-right edge of the screen */
		MIDDLE_RIGHT,
		/** Bottom-left corner of the screen */
		BOTTOM_LEFT,
		/** Bottom-center of the screen */
		BOTTOM_CENTER,
		/** Bottom-right corner of the screen */
		BOTTOM_RIGHT
	}

	/**
	 * Horizontal text alignment relative to the anchor point.
	 */
	public enum TextAlign {
		/** Align text to the left of the anchor */
		LEFT,
		/** Center text at the anchor */
		CENTER,
		/** Align text to the right of the anchor */
		RIGHT
	}

	private boolean shadow = true;
	private TextAnchor anchor = TextAnchor.TOP_CENTER;
	private TextAlign align = TextAlign.CENTER;
	private float offsetX = 0f;
	private float offsetY = 55f;
	private float scale = 1f;
	private int fadeInTicks = 0;
	private int fadeOutTicks = 0;
	private boolean background = false;
	private Float typewriterSpeed;
	private boolean typewriterCenter;
	private Integer wrapMaxWidth;

	/**
	 * Creates a new immersive message builder with default settings.
	 * Defaults: shadow=true, anchor=TOP_CENTER, align=CENTER, offsetY=55f, scale=1f, no fade or background.
	 *
	 * @return a new builder instance ready for configuration
	 */
	public static MatthiesenLibImmersiveMessageBuilder create() {
		return new MatthiesenLibImmersiveMessageBuilder();
	}

	/**
	 * Configures whether text shadows are rendered.
	 *
	 * @param shadow {@code true} to render shadows (default), {@code false} to disable
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder shadow(boolean shadow) {
		this.shadow = shadow;
		return this;
	}

	/**
	 * Sets the screen anchor point where the message will be positioned.
	 *
	 * @param anchor the anchor position (default: {@code TOP_CENTER})
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder anchor(TextAnchor anchor) {
		if (anchor != null) {
			this.anchor = anchor;
		}
		return this;
	}

	/**
	 * Sets the horizontal text alignment relative to the anchor point.
	 *
	 * @param align the text alignment (default: {@code CENTER})
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder align(TextAlign align) {
		if (align != null) {
			this.align = align;
		}
		return this;
	}

	/**
	 * Sets the pixel offset from the anchor point.
	 * Positive X moves right, positive Y moves down.
	 *
	 * @param x horizontal offset in pixels (default: 0)
	 * @param y vertical offset in pixels (default: 55)
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder offset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}

	/**
	 * Sets the text scale multiplier.
	 *
	 * @param scale the scale factor (1.0 is normal, &gt;1.0 is larger, &lt;1.0 is smaller; default: 1.0)
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder scale(float scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * Sets the number of ticks the message takes to fade in from invisible to full opacity.
	 *
	 * @param ticks fade-in duration in ticks (default: 0, must be non-negative)
	 * @return this builder for method chaining
	 * @throws IllegalArgumentException if {@code ticks} is negative
	 */
	public MatthiesenLibImmersiveMessageBuilder fadeInTicks(int ticks) {
		if (ticks < 0) {
			throw new IllegalArgumentException("fadeInTicks must be non-negative");
		}
		this.fadeInTicks = ticks;
		return this;
	}

	/**
	 * Sets the number of ticks the message takes to fade out after the visible duration expires.
	 *
	 * @param ticks fade-out duration in ticks (default: 0, must be non-negative)
	 * @return this builder for method chaining
	 * @throws IllegalArgumentException if {@code ticks} is negative
	 */
	public MatthiesenLibImmersiveMessageBuilder fadeOutTicks(int ticks) {
		if (ticks < 0) {
			throw new IllegalArgumentException("fadeOutTicks must be non-negative");
		}
		this.fadeOutTicks = ticks;
		return this;
	}

	/**
	 * Enables or disables the background panel behind the message text.
	 *
	 * @param enabled {@code true} to show a background panel, {@code false} to hide it (default: false)
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder background(boolean enabled) {
		this.background = enabled;
		return this;
	}

	/**
	 * Enables a typewriter effect that reveals text character-by-character during message display.
	 *
	 * @param speed characters revealed per tick (default: none/disabled)
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder typewriter(float speed) {
		return typewriter(speed, false);
	}

	/**
	 * Enables a typewriter effect with optional centering of the reveal animation.
	 *
	 * @param speed characters revealed per tick
	 * @param center {@code true} to center the text while revealing, {@code false} to align left
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder typewriter(float speed, boolean center) {
		this.typewriterSpeed = speed;
		this.typewriterCenter = center;
		return this;
	}

	/**
	 * Sets the maximum text wrapping width in pixels.
	 *
	 * @param maxWidth the wrap width in pixels, or -1 to disable wrapping (default: disabled)
	 * @return this builder for method chaining
	 */
	public MatthiesenLibImmersiveMessageBuilder wrap(int maxWidth) {
		this.wrapMaxWidth = maxWidth;
		return this;
	}

	// ================= Getters =================

	/**
	 * Gets the shadow setting.
	 * @return {@code true} if shadows are enabled
	 */
	public boolean isShadow() {
		return shadow;
	}

	/**
	 * Gets the anchor position.
	 * @return the screen anchor point
	 */
	public TextAnchor getAnchor() {
		return anchor;
	}

	/**
	 * Gets the text alignment.
	 * @return the horizontal text alignment
	 */
	public TextAlign getAlign() {
		return align;
	}

	/**
	 * Gets the horizontal offset.
	 * @return the X offset in pixels
	 */
	public float getOffsetX() {
		return offsetX;
	}

	/**
	 * Gets the vertical offset.
	 * @return the Y offset in pixels
	 */
	public float getOffsetY() {
		return offsetY;
	}

	/**
	 * Gets the text scale.
	 * @return the scale multiplier
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Gets the fade-in duration.
	 * @return the fade-in ticks
	 */
	public int getFadeInTicks() {
		return fadeInTicks;
	}

	/**
	 * Gets the fade-out duration.
	 * @return the fade-out ticks
	 */
	public int getFadeOutTicks() {
		return fadeOutTicks;
	}

	/**
	 * Gets the background visibility.
	 * @return {@code true} if background is enabled
	 */
	public boolean isBackground() {
		return background;
	}

	/**
	 * Gets the typewriter speed if enabled.
	 * @return the characters per tick, or {@code null} if typewriter is disabled
	 */
	public Float getTypewriterSpeed() {
		return typewriterSpeed;
	}

	/**
	 * Gets whether typewriter centering is enabled.
	 * @return {@code true} if text centers during typewriter reveal
	 */
	public boolean isTypewriterCenter() {
		return typewriterCenter;
	}

	/**
	 * Gets the text wrap width if enabled.
	 * @return the wrap width in pixels, or {@code null} if wrapping is disabled
	 */
	public Integer getWrapMaxWidth() {
		return wrapMaxWidth;
	}

	/**
	 * Applies this builder's configuration to an Ember {@code ImmersiveMessage} instance
	 * via reflective method invocation. This allows the common builder to work without
	 * direct imports of platform-specific Ember APIs.
	 *
	 * <p>Reflective invocation is fault-tolerant: if a setter is unavailable or the target
	 * class doesn't expose it across versions, the call is silently ignored.
	 *
	 * @param immersiveMessage the {@code ImmersiveMessage} to configure (typically from {@code ImmersiveMessage.builder(...)})
	 */
	public void applyTo(Object immersiveMessage) {
		if (immersiveMessage == null) {
			return;
		}

		invoke(immersiveMessage, "shadow", new Class<?>[] { boolean.class }, shadow);
		invokeEnum(immersiveMessage, "anchor", "net.tysontheember.emberstextapi.immersivemessages.api.TextAnchor", anchor);
		invokeEnum(immersiveMessage, "align", "net.tysontheember.emberstextapi.immersivemessages.api.TextAlign", align);
		invoke(immersiveMessage, "offset", new Class<?>[] { float.class, float.class }, offsetX, offsetY);
		invoke(immersiveMessage, "scale", new Class<?>[] { float.class }, scale);
		invoke(immersiveMessage, "fadeInTicks", new Class<?>[] { int.class }, fadeInTicks);
		invoke(immersiveMessage, "fadeOutTicks", new Class<?>[] { int.class }, fadeOutTicks);

		if (background) {
			invoke(immersiveMessage, "background", new Class<?>[] { boolean.class }, true);
		}

		if (typewriterSpeed != null) {
			invoke(immersiveMessage, "typewriter", new Class<?>[] { float.class, boolean.class }, typewriterSpeed, typewriterCenter);
		}

		if (wrapMaxWidth != null) {
			invoke(immersiveMessage, "wrap", new Class<?>[] { int.class }, wrapMaxWidth);
		}

	}

	/**
	 * Reflectively invokes a method on the target object with the given parameter types and arguments.
	 * Silently ignores {@code ReflectiveOperationException} to tolerate method availability differences across versions.
	 *
	 * @param target the object on which to invoke the method
	 * @param methodName the name of the method to invoke
	 * @param parameterTypes the method parameter types
	 * @param args the method arguments
	 */
	private static void invoke(Object target, String methodName, Class<?>[] parameterTypes, Object... args) {
		try {
			Method method = target.getClass().getMethod(methodName, parameterTypes);
			method.invoke(target, args);
		} catch (ReflectiveOperationException ignored) {
			// The target implementation may not expose every setter across versions.
		}
	}

	/**
	 * Reflectively invokes an enum-typed method on the target object, translating the common-layer
	 * enum value to the platform-specific enum instance before invocation.
	 *
	 * @param target the object on which to invoke the method
	 * @param methodName the name of the method to invoke
	 * @param enumClassName the fully-qualified class name of the platform-specific enum
	 * @param value the common-layer enum value to translate
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static void invokeEnum(Object target, String methodName, String enumClassName, Enum<?> value) {
		if (value == null) {
			return;
		}

		try {
			ClassLoader loader = target.getClass().getClassLoader();
			Class<?> enumClass = Class.forName(enumClassName, true, loader);
			Object translated = Enum.valueOf((Class) enumClass.asSubclass(Enum.class), value.name());
			invoke(target, methodName, new Class<?>[] { enumClass }, translated);
		} catch (ReflectiveOperationException | IllegalArgumentException ignored) {
			// If the enum or setter is unavailable we leave the default values in place.
		}
	}
}
