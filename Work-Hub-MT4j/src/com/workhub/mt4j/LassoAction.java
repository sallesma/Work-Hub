package com.workhub.mt4j;

import java.util.Random;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.clusters.Cluster;
import org.mt4j.components.clusters.ClusterManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.IdragClusterable;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class LassoAction  implements IGestureEventListener{
	private ClusterManager clusterManager;
	private MTCanvas canvas;
	private PApplet applet;
	private WorkHubScene scene;

	public LassoAction(PApplet pa, WorkHubScene scene, ClusterManager clustermgr, MTCanvas canvas){
		this.applet = pa;
		this.clusterManager = clustermgr;
		this.canvas = canvas;
		this.scene = scene;
	}

	public boolean processGestureEvent(MTGestureEvent g) {
		if (g instanceof LassoEvent){
			LassoEvent lassoEvent = (LassoEvent)g;
			switch (lassoEvent.getId()) {
			case MTGestureEvent.GESTURE_DETECTED:
				canvas.addChild(lassoEvent.getSelectionPoly());
				break;
			case MTGestureEvent.GESTURE_UPDATED:
				break;
			case MTGestureEvent.GESTURE_ENDED:
				IdragClusterable[] selectedComps = lassoEvent.getClusteredComponents();
				
				if (selectedComps.length > 1){
					final ElementGroupView elementGroupView = new ElementGroupView(applet, scene, lassoEvent.getSelectionPoly());
					elementGroupView.attachCamera(selectedComps[0].getViewingCamera());
					
					elementGroupView.registerInputProcessor(new DragProcessor(applet));
					elementGroupView.addGestureListener(DragProcessor.class, new DefaultDragAction());
					
//					cluster.addGestureListener(DragProcessor.class, new InertiaDragAction());
					
					elementGroupView.registerInputProcessor(new RotateProcessor(applet));
					elementGroupView.addGestureListener(RotateProcessor.class, new DefaultRotateAction());
					
					elementGroupView.registerInputProcessor(new ScaleProcessor(applet));
					elementGroupView.addGestureListener(ScaleProcessor.class,  new DefaultScaleAction());
					
					//TODO : rajouter le menu contextuel sur le tapAndHold
					elementGroupView.registerInputProcessor(new TapAndHoldProcessor(applet, 700));
					elementGroupView.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer((MTApplication) applet, canvas));
					elementGroupView.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
						public boolean processGestureEvent(MTGestureEvent ge) {
							TapAndHoldEvent tahe = (TapAndHoldEvent)ge;
							switch (tahe.getId()) {
							case TapAndHoldEvent.GESTURE_DETECTED:
								break;
							case TapAndHoldEvent.GESTURE_UPDATED:
								break;
							case TapAndHoldEvent.GESTURE_ENDED:
								if (tahe.isHoldComplete()){
									elementGroupView.openContextualMenu(tahe.getLocationOnScreen());
								}
								break;
							default:
								break;
							}
							return false;
						}
					});
					
					lassoEvent.getSelectionPoly().setFillColor(new MTColor(100,150,250, 50));
					
					lassoEvent.getSelectionPoly().setGestureAllowance(DragProcessor.class, true);
					lassoEvent.getSelectionPoly().setGestureAllowance(RotateProcessor.class, true);
					lassoEvent.getSelectionPoly().setGestureAllowance(ScaleProcessor.class, true);
					
					
					//Regroupe les éléments (en plus du traitement par défault
					float averageX = 0;
					float averageY = 0;
					float count = 0;
					
					int n = Integer.MAX_VALUE;
					for (IdragClusterable currentComp : selectedComps){
						if (currentComp instanceof MTComponent){//Add selected comps to selection - RIGHT NOW ONLY SUPPORTS INSTANCES OF MTCOMPONENT!
							MTComponent mtCurrentComp = (MTComponent)currentComp;
							mtCurrentComp.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener(){
								public void stateChanged(StateChangeEvent evt) {
									if (evt.getSource() instanceof MTComponent) {
										MTComponent sourceComp = (MTComponent) evt.getSource();
										
										Cluster clusterOfComponent = clusterManager.getCluster(sourceComp);
										if (clusterOfComponent != null){
											((IdragClusterable)sourceComp).setSelected(false);
											clusterOfComponent.removeChild(sourceComp);
											
											if (clusterOfComponent.getChildCount() <= 2){
												clusterManager.removeCluster(clusterOfComponent);
											}else{
												clusterOfComponent.packClusterPolygon();
											}
										}
									}
								}
							});

							//Remove comp from former selection if it is in a new selection
							Cluster formerSelection = clusterManager.getCluster(currentComp);
							if (formerSelection != null){
								formerSelection.removeChild(mtCurrentComp);
								if (formerSelection.getChildCount() <= 2){ //2 because the selection polygon is also always in the selection
									clusterManager.removeCluster(formerSelection);
								}else{
									formerSelection.packClusterPolygon();
								}
							}
							if (mtCurrentComp.getParent() != null){
								int indexInParentList = mtCurrentComp.getParent().getChildIndexOf(mtCurrentComp);
								if (indexInParentList < n){
									n = indexInParentList;
								}
							}
								
							elementGroupView.addChild(mtCurrentComp);
							currentComp.setSelected(true);
							
							//Calcul de la position moyenne
							((AbstractElementView) currentComp).setAnchor(PositionAnchor.UPPER_LEFT);
							Vector3D position = ((AbstractElementView) currentComp).getPosition(TransformSpace.GLOBAL);
							averageX += position.getX();
							averageY += position.getY();
							count++;
						}
					}
					
					averageX = averageX / count;
					averageY = averageY / count;
					Random r = new Random();
					for (IdragClusterable currentComp : selectedComps){
						int decalageX = 1 + r.nextInt(50 - 1);
						int decalageY = 1 + r.nextInt(50 - 1);
						((AbstractElementView)currentComp).tweenTranslateTo(averageX+decalageX, averageY+decalageY, 0, 500, 0.25f, 0.25f);
					}
					
					//Draw a convex hull around all selected shapes
					elementGroupView.packClusterPolygon();
					lassoEvent.getSelectionPoly().setLineStipple((short)0xDDDD);
					lassoEvent.getSelectionPoly().setStrokeColor(new MTColor(0,0,0,255));
					
					if (selectedComps[0] instanceof MTComponent && ((MTComponent)selectedComps[0]).getParent() != null) {
							MTComponent firstSelectedComp = (MTComponent)selectedComps[0];
							firstSelectedComp.getParent().addChild(n, lassoEvent.getSelectionPoly());
					}
					
					//Add selection to selection manager
					clusterManager.addCluster(elementGroupView);
				//IF exactly 1 component is selected and its already part of an selection remove it from it without making a new selection with it
				}else if (selectedComps.length == 1){ 
					for (IdragClusterable currentComp : selectedComps){
						if (currentComp instanceof MTComponent){
							Cluster formerSelection = clusterManager.getCluster(currentComp);
							if (formerSelection != null){
								currentComp.setSelected(false);
								formerSelection.removeChild((MTComponent)currentComp);
								if (formerSelection.getChildCount() <= 2){
									clusterManager.removeCluster(formerSelection);
								}else{
									formerSelection.packClusterPolygon();
								}
							}
						}
					}
					//Remove the Selection Polygon from the canvas when only 1 component is selected
					clusterManager.removeClusterPolyFromCanvas(lassoEvent.getSelectionPoly());
				}
				//If no comp is selected, just remove the selection polygon from canvas
				else if (selectedComps.length < 1){ 
					//Remove the Selection Polygon from the canvas when no component is selected
					clusterManager.removeClusterPolyFromCanvas(lassoEvent.getSelectionPoly());
				}
				break;
			}
		}
		return false;
	}
}
