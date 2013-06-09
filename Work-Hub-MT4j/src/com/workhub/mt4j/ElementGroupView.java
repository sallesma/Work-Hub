package com.workhub.mt4j;

import org.mt4j.components.clusters.Cluster;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class ElementGroupView extends Cluster {
	private WorkHubScene scene;

	public ElementGroupView(PApplet applet, final WorkHubScene scene, MTPolygon selectionPolygon) {
		super(applet, selectionPolygon);
		this.scene = scene;

		addGestureListener(DragProcessor.class, new IGestureEventListener() {

			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				if(de.getId() == DragEvent.GESTURE_ENDED) {
					String intersection = scene.testShortCutIntersection(de.getTo());
					if(intersection != null) {
						switch(intersection) {
						case MT4JConstants.BUTTON_ID_MASQUER :
							ElementGroupView.this.destroy();
							break;
						case MT4JConstants.BUTTON_ID_ENVOYER :
							ContextMenu.exportLocation.add(new ExportData(ElementGroupView.this, de.getTo()));
							JadeInterface.getInstance().getNeightbourgList();
							break;
						default :
							break;
						}
					}
				}
				return false;
			}
		});
	}
	public void openContextualMenu(Vector3D locationOnScreen) {
		ContextMenu contextMenu = new ContextMenu(this, (int)locationOnScreen.x, (int)locationOnScreen.y, getRenderer(), scene, MT4JConstants.CONTEXT_GROUP_MENU);
		scene.getCanvas().addChild(contextMenu);
	}
}
