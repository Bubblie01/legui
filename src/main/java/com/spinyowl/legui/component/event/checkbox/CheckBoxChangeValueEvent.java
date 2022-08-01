package com.spinyowl.legui.component.event.checkbox;

import com.spinyowl.legui.component.CheckBox;
import com.spinyowl.legui.component.Frame;
import com.spinyowl.legui.event.Event;
import com.spinyowl.legui.system.context.Context;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Event generated by default event listeners which shows that checkbox value was changed.
 */
public class CheckBoxChangeValueEvent<T extends CheckBox> extends Event<T> {

  private final boolean oldValue;
  private final boolean newValue;

  public CheckBoxChangeValueEvent(T component, Context context, Frame frame, boolean oldValue,
      boolean newValue) {
    super(component, context, frame);
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /**
   * Returns old value.
   *
   * @return old value.
   * @deprecated since 2.0.0 - use {@link #getOldValue()}
   */
  @Deprecated
  public boolean isOldValue() {
    return oldValue;
  }

  /**
   * Returns new value.
   *
   * @return new value.
   * @deprecated since 2.0.0 - use {@link #getNewValue()}
   */
  @Deprecated
  public boolean isNewValue() {
    return newValue;
  }

  /**
   * Returns old value.
   *
   * @return old value.
   */
  public boolean getOldValue() {
    return oldValue;
  }

  /**
   * Returns new value.
   *
   * @return new value.
   */
  public boolean getNewValue() {
    return newValue;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("oldValue", oldValue)
        .append("newValue", newValue)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CheckBoxChangeValueEvent<?> that = (CheckBoxChangeValueEvent<?>) o;

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(oldValue, that.oldValue)
        .append(newValue, that.newValue)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(oldValue)
        .append(newValue)
        .toHashCode();
  }
}