package com.spinyowl.legui.component;

import static com.spinyowl.legui.component.TabbedPanel.TabStripPosition.BOTTOM;
import static com.spinyowl.legui.component.TabbedPanel.TabStripPosition.RIGHT;
import static com.spinyowl.legui.component.TabbedPanel.TabStripPosition.TOP;
import static com.spinyowl.legui.event.MouseClickEvent.MouseClickAction.CLICK;
import static com.spinyowl.legui.style.Style.DisplayType.FLEX;
import static com.spinyowl.legui.style.Style.PositionType.RELATIVE;
import static com.spinyowl.legui.style.flex.FlexStyle.FlexDirection.COLUMN;
import static com.spinyowl.legui.style.flex.FlexStyle.FlexDirection.COLUMN_REVERSE;
import static com.spinyowl.legui.style.flex.FlexStyle.FlexDirection.ROW;
import static com.spinyowl.legui.style.flex.FlexStyle.FlexDirection.ROW_REVERSE;

import com.spinyowl.legui.component.event.button.ButtonWidthChangeEvent;
import com.spinyowl.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import com.spinyowl.legui.event.MouseClickEvent;
import com.spinyowl.legui.style.Style;
import com.spinyowl.legui.style.color.ColorConstants;
import com.spinyowl.legui.style.flex.FlexStyle.AlignItems;
import com.spinyowl.legui.style.flex.FlexStyle.JustifyContent;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TabbedPanel extends Component {

  private static final float DEFAULT_TAB_HEIGHT = 30F;
  private static final float DEFAULT_TAB_WIDTH = 90f;

  private Component container;
  private TabStrip strip;

  private final List<Tab> tabs = new CopyOnWriteArrayList<>();
  private final Map<Tab, ToggleButton> tabButtons = new ConcurrentHashMap<>();

  private float tabWidth;
  private float tabHeight;
  private TabStripPosition tabStripPosition;

  /**
   * Creates default tabbed panel with scrollable tab strip. Strip position will be top, tab height
   * - 28px, tab width - 60px.
   *
   * <p>Scrollable tab strip subtracts space required for scroll bar depending on strip position
   * from tab width or tab height.
   */
  public TabbedPanel() {
    this(TOP, DEFAULT_TAB_WIDTH, DEFAULT_TAB_HEIGHT);
  }

  /**
   * Creates tabbed panel with default
   *
   * @param tabStripPosition tab strip position - position where tab strip should be placed.
   * @param tabWidth minimum tab width.
   * @param tabHeight minimum tab height.
   */
  public TabbedPanel(TabStripPosition tabStripPosition, float tabWidth, float tabHeight) {
    this.tabStripPosition = tabStripPosition;
    this.tabWidth = tabWidth;
    this.tabHeight = tabHeight;
    initialize();
  }

  private void initialize() {
    strip = new ScrollTabStrip(this, tabStripPosition, tabWidth, tabHeight);
    this.add(strip);

    createContainer();
    this.add(container);

    updateStyles();
  }

  private void updateStyles() {
    Style style = this.getStyle();
    style.setDisplay(FLEX);
    style.setMinWidth(50f);
    style.setMinHeight(50f);
    style.setPadding(0f);
    style.getFlexStyle().setAlignItems(AlignItems.STRETCH);

    if (TOP == tabStripPosition) {
      style.getFlexStyle().setFlexDirection(COLUMN);
    } else if (BOTTOM == tabStripPosition) {
      style.getFlexStyle().setFlexDirection(COLUMN_REVERSE);
    } else if (RIGHT == tabStripPosition) {
      style.getFlexStyle().setFlexDirection(ROW_REVERSE);
    } else {
      style.getFlexStyle().setFlexDirection(ROW);
    }
    setStripStyles();
  }

  private void setStripStyles() {
    strip.setFocusable(false);
    strip.getStyle().setPosition(RELATIVE);
    strip.getStyle().getFlexStyle().setFlexGrow(1);
    strip.getStyle().getFlexStyle().setFlexShrink(1);
    strip.getStyle().getBackground().setColor(ColorConstants.lightGray());
    if (TOP.equals(tabStripPosition) || BOTTOM.equals(tabStripPosition)) {
      strip.getStyle().setWidth(null);
      strip.getStyle().setMinWidth(null);
      strip.getStyle().setMaxWidth(null);

      strip.getStyle().setHeight(tabHeight);
      strip.getStyle().setMinHeight(tabHeight);
      strip.getStyle().setMaxHeight(tabHeight);
    } else {
      strip.getStyle().setWidth(tabWidth);
      strip.getStyle().setMinWidth(tabWidth);
      strip.getStyle().setMaxWidth(tabWidth);

      strip.getStyle().setHeight(null);
      strip.getStyle().setMinHeight(null);
      strip.getStyle().setMaxHeight(null);
    }
    strip.updateStyles();
    for (ToggleButton button : tabButtons.values()) {
      strip.updateTabButtonStyles(button);
    }
  }

  private void createContainer() {
    container = new Panel();
    container.setTabFocusable(false);
    container.getStyle().getFlexStyle().setFlexShrink(1);
    container.getStyle().getFlexStyle().setFlexGrow(1);
    container.getStyle().setPosition(RELATIVE);
    container.getStyle().setDisplay(FLEX);
  }

  public void addTab(Tab tab) {
    tabs.add(tab);
    // add tab toggle button
    ToggleButton tabButton = createTabButton(tab);
    tabButtons.put(tab, tabButton);
    strip.addTabButton(tabButton);
    strip.updateStyles();
    for (ToggleButton button : tabButtons.values()) {
      strip.updateTabButtonStyles(button);
    }
    // add to tab content to container
    tab.tabComponent.getStyle().getFlexStyle().setFlexGrow(1);
    tab.tabComponent.getStyle().setPosition(RELATIVE);

    setActiveTab(tab);
  }

  public void setActiveTab(Tab tab) {
    if (tabs.contains(tab)) {
      tabButtons.forEach(
          (t, button) -> {
            container.remove(t.tabComponent);
            button.setToggled(false);
          });
      tabButtons.get(tab).setToggled(true);
      container.add(tab.tabComponent);
    }
  }

  public void setTabWidth(int tabWidth) {
    this.tabWidth = tabWidth;
    this.strip.setTabWidth(tabWidth);
    updateStyles();
  }

  public void setTabHeight(float tabHeight) {
    this.tabHeight = tabHeight;
    this.strip.setTabHeight(tabHeight);
    updateStyles();
  }

  public void setTabStripPosition(TabStripPosition tabStripPosition) {
    this.tabStripPosition = tabStripPosition;
    strip.setTabStripPosition(tabStripPosition);
    updateStyles();
  }

  public TabStripPosition getTabStripPosition() {
    return tabStripPosition;
  }

  private ToggleButton createTabButton(Tab tab) {
    ToggleButton tabButton = new ToggleButton(tab.tabName);
    tabButton
        .getListenerMap()
        .addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener());
    strip.updateTabButtonStyles(tabButton);
    tabButton
        .getListenerMap()
        .addListener(
            MouseClickEvent.class,
            event -> {
              if (CLICK.equals(event.getAction())) {
                if (tabButton.isToggled()) {
                  tabButtons.values().stream()
                      .filter(b -> b != tabButton)
                      .forEach(b -> b.setToggled(false));
                } else {
                  tabButton.setToggled(true);
                }
                setActiveTab(tab);
              }
            });
    return tabButton;
  }

  public int tabCount() {
    return tabs.size();
  }

  public Tab getTab(int index) {
    return tabs.get(index);
  }

  public Tab getCurrentTab() {
    return tabButtons.entrySet().stream()
        .filter(e -> e.getValue().isToggled())
        .findFirst()
        .map(Entry::getKey)
        .orElse(null);
  }

  public void removeTab(int index) {
    Tab tabToRemove = tabs.get(index);
    this.removeTab(tabToRemove);
  }

  public void removeTab(Tab tabToRemove) {
    int index = tabs.indexOf(tabToRemove);
    tabs.remove(tabToRemove);
    ToggleButton button = tabButtons.remove(tabToRemove);
    strip.removeTabButton(button);
    strip.updateStyles();
    container.remove(tabToRemove.tabComponent);

    if (!tabs.isEmpty()) {
      if (index < tabs.size()) {
        setActiveTab(tabs.get(index));
      } else {
        setActiveTab(tabs.get(index - 1));
      }
    }

    // remove button from tab strip
    // remove content from container
  }

  public abstract static class TabStrip extends Component {

    public abstract TabStripPosition getTabStripPosition();

    public abstract void setTabStripPosition(TabStripPosition tabStripPosition);

    public abstract void addTabButton(Button tabButton);

    public abstract void removeTabButton(Button tabButton);

    public abstract void updateStyles();

    public abstract void updateTabButtonStyles(Button tabButton);

    public abstract void setTabWidth(float tabSize);

    public abstract void setTabHeight(float tabSize);
  }

  public static class ScrollTabStrip extends TabStrip {
    private final TabbedPanel tabbedPanel;
    private ScrollablePanel scrollablePanel;
    private TabStripPosition tabStripPosition;
    private float tabWidth;
    private float tabHeight;

    public ScrollTabStrip(
        TabbedPanel tabbedPanel,
        TabStripPosition tabStripPosition,
        float tabWidth,
        float tabHeight) {
      this.tabbedPanel = tabbedPanel;
      this.tabStripPosition = tabStripPosition;
      this.tabWidth = tabWidth;
      this.tabHeight = tabHeight;
      initialize();
    }

    private void initialize() {
      this.getStyle().setDisplay(FLEX);

      scrollablePanel = new TabStripScrollablePanel();
      scrollablePanel.getStyle().setPosition(RELATIVE);
      scrollablePanel.getStyle().getFlexStyle().setFlexShrink(1);
      scrollablePanel.getStyle().getFlexStyle().setFlexGrow(1);
      scrollablePanel.getStyle().getBackground().setColor(ColorConstants.transparent());
      scrollablePanel.getStyle().setBorder(null);
      scrollablePanel.getContainer().getStyle().setBorder(null);
      scrollablePanel
          .getViewport()
          .getStyle()
          .getBackground()
          .setColor(ColorConstants.white().mul(1, 1, 1, 0.5f));

      this.add(scrollablePanel);
      updateStyles();
    }

    public TabStripPosition getTabStripPosition() {
      return tabStripPosition;
    }

    public void setTabStripPosition(TabStripPosition tabStripPosition) {
      this.tabStripPosition = tabStripPosition;
      this.updateStyles();
    }

    public void updateStyles() {
      Component container = scrollablePanel.getContainer();
      Style containerStyle = container.getStyle();
      containerStyle.setDisplay(FLEX);
      containerStyle.getBackground().setColor(ColorConstants.transparent());
      containerStyle.getFlexStyle().setJustifyContent(JustifyContent.FLEX_START);
      float width;
      float height;
      if (tabStripPosition == BOTTOM || tabStripPosition == TOP) {
        scrollablePanel.setHorizontalScrollBarVisible(true);
        scrollablePanel.setVerticalScrollBarVisible(false);
        width = Math.max(container.count() * tabWidth, tabbedPanel.getSize().x);
        height = tabHeight - scrollablePanel.getHorizontalScrollBar().getSize().y;
        containerStyle.getFlexStyle().setFlexDirection(ROW);
      } else {
        scrollablePanel.setHorizontalScrollBarVisible(false);
        scrollablePanel.setVerticalScrollBarVisible(true);
        width = tabWidth - scrollablePanel.getVerticalScrollBar().getSize().x;
        height = Math.max(container.count() * tabHeight, tabbedPanel.getSize().y);
        containerStyle.getFlexStyle().setFlexDirection(COLUMN);
      }
      container.setSize(width, height);
      container.getStyle().setWidth(width);
      container.getStyle().setHeight(height);
      for (Button tab : tabbedPanel.tabButtons.values()) {
        updateTabButtonStyles(tab);
      }
    }

    public void updateTabButtonStyles(Button tabButton) {
      tabButton.getStyle().getFlexStyle().setFlexGrow(1);
      tabButton.getStyle().getFlexStyle().setFlexShrink(0);
      tabButton.getStyle().setPosition(RELATIVE);
      tabButton.getStyle().setPadding(2F, 4F);
      if (tabStripPosition == TOP || tabStripPosition == BOTTOM) {
        tabButton.getStyle().setMinWidth(tabWidth);
        tabButton.getStyle().setWidth(tabWidth);
        tabButton.getStyle().setMaxWidth(tabWidth);
        tabButton.getStyle().setMinHeight(null);
        tabButton.getStyle().setHeight(null);
        tabButton.getStyle().setMaxHeight(null);
      } else if (tabStripPosition == RIGHT || tabStripPosition == TabStripPosition.LEFT) {
        tabButton.getStyle().setMinWidth(null);
        tabButton.getStyle().setWidth(null);
        tabButton.getStyle().setMaxWidth(null);
        tabButton.getStyle().setMinHeight(tabHeight);
        tabButton.getStyle().setHeight(tabHeight);
        tabButton.getStyle().setMaxHeight(tabHeight);
      }
    }

    public void setTabWidth(float tabWidth) {
      this.tabWidth = tabWidth;
    }

    public void addTabButton(Button tabButton) {
      scrollablePanel.getContainer().add(tabButton);
    }

    public void removeTabButton(Button tabButton) {
      scrollablePanel.getContainer().remove(tabButton);
    }

    public void setTabHeight(float tabHeight) {
      this.tabHeight = tabHeight;
    }

    private static final class TabStripScrollablePanel extends ScrollablePanel {}
  }

  public static class Tab {
    private final String uid = UUID.randomUUID().toString();
    private final String tabName;
    private final Component tabComponent;

    public Tab(String tabName, Component tabComponent) {
      this.tabName = tabName;
      this.tabComponent = tabComponent;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Tab tab = (Tab) o;

      return new EqualsBuilder().append(uid, tab.uid).isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37).append(uid).toHashCode();
    }
  }

  public enum TabStripPosition {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TabbedPanel that = (TabbedPanel) o;

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(tabWidth, that.tabWidth)
        .append(tabHeight, that.tabHeight)
        .append(container, that.container)
        .append(strip, that.strip)
        .append(tabs, that.tabs)
        .append(tabButtons, that.tabButtons)
        .append(tabStripPosition, that.tabStripPosition)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(container)
        .append(strip)
        .append(tabs)
        .append(tabButtons)
        .append(tabWidth)
        .append(tabHeight)
        .append(tabStripPosition)
        .toHashCode();
  }
}
