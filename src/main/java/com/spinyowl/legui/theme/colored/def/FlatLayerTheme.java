package com.spinyowl.legui.theme.colored.def;

import com.spinyowl.legui.component.Layer;
import com.spinyowl.legui.theme.colored.FlatColoredTheme.FlatColoredThemeSettings;

/**
 * Dark LayerContainer Theme for all layer containers. Used to make layer container dark.
 *
 * @param <T> {@link Layer} subclasses.
 */
public class FlatLayerTheme<T extends Layer> extends FlatBorderlessTransparentTheme<T> {

  /** Default constructor. Settings should be specified before using this theme. */
  public FlatLayerTheme() {}

  public FlatLayerTheme(FlatColoredThemeSettings settings) {
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
  }
}
