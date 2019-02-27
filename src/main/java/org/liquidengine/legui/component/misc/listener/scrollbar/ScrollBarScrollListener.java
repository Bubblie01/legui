package org.liquidengine.legui.component.misc.listener.scrollbar;

import org.liquidengine.legui.component.ScrollBar;
import org.liquidengine.legui.component.event.scrollbar.ScrollBarChangeValueEvent;
import org.liquidengine.legui.event.ScrollEvent;
import org.liquidengine.legui.listener.ScrollEventListener;
import org.liquidengine.legui.listener.processor.EventProcessor;

/**
 * Default mouse scroll event listener for scrollbar. Generates {@link ScrollBarChangeValueEvent} event.
 */
public class ScrollBarScrollListener implements ScrollEventListener {

    public void process(ScrollEvent event) {
        ScrollBar scrollBar = (ScrollBar) event.getTargetComponent();
        float maxValue = scrollBar.getMaxValue();
        float minValue = scrollBar.getMinValue();
        float curValue = scrollBar.getCurValue();
        float visibleAmount = scrollBar.getVisibleAmount();
        float valueRange = scrollBar.getMaxValue() - scrollBar.getMinValue();

        if (valueRange - visibleAmount < 0.001f) {
            return;
        }

        float newVal = (float) (curValue - scrollBar.getScrollStep() * event.getYoffset() * visibleAmount * valueRange / (valueRange - visibleAmount));

        if (newVal > maxValue) {
            newVal = maxValue;
        }
        if (newVal < minValue) {
            newVal = minValue;
        }

        EventProcessor.getInstance().pushEvent(new ScrollBarChangeValueEvent<>(scrollBar, event.getContext(), event.getFrame(), curValue, newVal));
        scrollBar.setCurValue(newVal);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj == this || obj.getClass() == this.getClass());
    }
}
