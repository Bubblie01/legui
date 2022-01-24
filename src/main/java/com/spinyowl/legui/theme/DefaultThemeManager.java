package com.spinyowl.legui.theme;

import com.spinyowl.legui.component.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Default implementation of theme manager. */
public class DefaultThemeManager extends ThemeManager {

  private final Map<Class<? extends Component>, AbstractTheme<? extends Component>> themeMap =
      new ConcurrentHashMap<>();

  private final AbstractTheme defaultComponentTheme =
      new AbstractTheme() {
        @Override
        public void apply(Component component) {
          // do nothing
        }
      };

  /** Returns theme for specified component class. */
  @Override
  public <T extends Component> AbstractTheme<T> getComponentTheme(Class<T> clazz) {
    if (clazz == null) {
      throw new IllegalArgumentException("Class cannot be null!");
    }
    return this.<T, AbstractTheme<T>>cycledSearch(clazz, themeMap, defaultComponentTheme);
  }

  /** Used to set theme per component class. */
  @Override
  public <T extends Component> void setComponentTheme(Class<T> clazz, AbstractTheme<T> theme) {
    if (theme == null) {
      throw new IllegalArgumentException("Theme cannot be null!");
    }
    if (clazz == null) {
      throw new IllegalArgumentException("Class cannot be null!");
    }
    themeMap.put(clazz, theme);
  }

  /** Used to search through class hierarcjy for appropriate theme. */
  protected <C, R> R cycledSearch(Class<C> componentClass, Map map, R defaultComponentTheme) {
    R componentTheme = null;
    Class cClass = componentClass;
    while (componentTheme == null) {
      componentTheme = ((Map<Class<C>, R>) map).get(cClass);
      if (cClass.isAssignableFrom(Component.class)) {
        break;
      }
      cClass = cClass.getSuperclass();
    }
    if (componentTheme == null) {
      componentTheme = defaultComponentTheme;
    }
    return componentTheme;
  }
}
