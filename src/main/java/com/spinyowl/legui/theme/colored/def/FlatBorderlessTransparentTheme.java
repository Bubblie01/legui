package com.spinyowl.legui.theme.colored.def;

import com.spinyowl.legui.component.Component;
import com.spinyowl.legui.component.Label;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.theme.colored.FlatColoredTheme.FlatColoredThemeSettings;

/**
 * Dark Label Theme for all labels. Used to make label dark.
 *
 * @param <T> {@link Label} subclasses.
 */
public class FlatBorderlessTransparentTheme<T extends Component> extends FlatComponentTheme<T> {

  /**
   * Default constructor. Settings should be specified before using this theme.
   */
  public FlatBorderlessTransparentTheme() {
  }

  public FlatBorderlessTransparentTheme(FlatColoredThemeSettings settings) {
    super(settings);
  }

  /**
   * Used to apply theme only for component and not apply for child components.
   *
   * @param component component to apply theme.
   */
  @Override
  public void apply(T component) {
    super.apply(component);
    component.getStyle().setBorder(null);
    component.getStyle().setShadow(null);
    component.getStyle().getBackground().setColor(ColorConstants.transparent());
  }
}
