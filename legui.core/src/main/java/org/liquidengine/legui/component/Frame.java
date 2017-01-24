package org.liquidengine.legui.component;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Frame {
    protected TooltipLayer tooltipLayer;
    protected Layer        componentLayer;
    protected List<Layer> layers = SetUniqueList.setUniqueList(new CopyOnWriteArrayList<>());

    public Frame() {
        initialize();
    }

    private void initialize() {
        tooltipLayer = new TooltipLayer();
        componentLayer = new Layer();
        componentLayer.topLayer = tooltipLayer;
        tooltipLayer.bottomLayer = componentLayer;
    }

    public void addLayer(Layer layer) {
        if (layer == null ||
                layer == tooltipLayer ||
                layer == componentLayer ||
                layer.display == this) {
            return;
        }

        if (layer.display != null) {
            layer.display.removeLayer(layer);
        }
        Layer previous;
        if (layers.isEmpty()) {
            previous = componentLayer;
        } else {
            previous = layers.get(layers.size() - 1);
        }
        layer.display = this;
        layer.bottomLayer = previous;
        layer.topLayer = tooltipLayer;
        previous.topLayer = layer;
        layers.add(layer);
    }

    public void removeLayer(Layer layer) {
        if (layer == null) {
            return;
        }
        layer.display = null;
        if (layers.contains(layer)) {
            Layer bottomLayer = layer.getBottomLayer();
            Layer topLayer    = layer.getTopLayer();
            bottomLayer.topLayer = topLayer;
            topLayer.bottomLayer = bottomLayer;
            layers.remove(layer);
        }
    }

    public Layer getComponentLayer() {
        return componentLayer;
    }

    public TooltipLayer getTooltipLayer() {
        return tooltipLayer;
    }

    public List<Layer> getLayers() {
        return new ArrayList<>(layers);
    }

    public List<Layer> getAllLayers() {
        ArrayList<Layer> layers = new ArrayList<>();
        layers.add(componentLayer);
        layers.addAll(this.layers);
        layers.add(tooltipLayer);
        return layers;
    }

    public ComponentContainer getContainer() {
        return componentLayer.getLayerFrame();
    }
}
