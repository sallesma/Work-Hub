package com.workhub.mt4j;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class ContextButton extends MTListCell {
	private MTTextField m_text;
	private final MTComponent m_source;
	
	public ContextButton(final PApplet applet, final WorkHubScene scene, MTComponent source, final String text) {
		super(MT4JConstants.CONTEXT_BUTTON_WIDTH, MT4JConstants.CONTEXT_BUTTON_HEIGHT, applet);
		
		IFont font = FontManager.getInstance().createFont(applet, "arial.ttf", 18);
		m_source = source;
		m_text = new MTTextField(0, 0, MT4JConstants.CONTEXT_BUTTON_WIDTH, MT4JConstants.CONTEXT_BUTTON_HEIGHT, font, applet);
		m_text.setFillColor(MTColor.AQUA);
		m_text.setText(text);
		addChild(m_text);

		addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					switch (cursorInputEvt.getId()) {
					case AbstractCursorInputEvt.INPUT_DETECTED:
						switch (text) {
						case MT4JConstants.CONTEXT_BUTTON_CLOSE:
							//just close the menu (instruction below)
							break;
						case MT4JConstants.CONTEXT_BUTTON_DELETE:
							m_source.removeFromParent();
							break;
						case MT4JConstants.CONTEXT_BUTTON_CREATE_TEXT:
							TextElementView textElement = new TextElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x,
									((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y,
									MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
							getParent().getParent().getParent().addChild(textElement);
							textElement.addLassoProcessor();
							break;
						case MT4JConstants.CONTEXT_BUTTON_CREATE_IMAGE:
							String imagePath = applet.selectInput();
							ImageElementView imageElement = new ImageElementView(imagePath, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, 
									((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, 
									MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
							getParent().getParent().getParent().addChild(imageElement);
							imageElement.addLassoProcessor();
							break;
						case MT4JConstants.CONTEXT_BUTTON_CREATE_LINK:
							LinkElementView linkElement = new LinkElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, 
									((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, 
									MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
							getParent().getParent().getParent().addChild(linkElement);
							linkElement.addLassoProcessor();
							break;
						case MT4JConstants.CONTEXT_BUTTON_CREATE_FILE:
							String filePath = applet.selectInput();
							FileElementView fileElement = new FileElementView(filePath, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, 
									((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, 
									MT4JConstants.ELEMENT_DEFAULT_WIDTH, MT4JConstants.ELEMENT_DEFAULT_HEIGHT, applet, scene);
							getParent().getParent().getParent().addChild(fileElement);
							fileElement.addLassoProcessor();
							break;
						case MT4JConstants.CONTEXT_BUTTON_VISUALIZE_ELEMENTS:
							break;
						case MT4JConstants.CONTEXT_BUTTON_EDIT_TITLE:
							((AbstractElementView) m_source).tryEditElementTitle();
							break;
						case MT4JConstants.CONTEXT_BUTTON_EDIT_CONTENT:
							((AbstractElementView) m_source).tryEditElementContent();
							break;
						case MT4JConstants.CONTEXT_BUTTON_SHARE:
							break;
						case MT4JConstants.CONTEXT_BUTTON_CHANGE_COLOR:
							PImage colPick = applet.loadImage("Image/colorcircle.png");
							final MTColorPicker colorWidget = new MTColorPicker(0, 0, colPick, applet);
							int colPickX = (int)cursorInputEvt.getPosX();
							int colPickY = (int)cursorInputEvt.getPosY();
							colorWidget.setPositionGlobal(new Vector3D(colPickX, colPickY));
							MT4JUtils.fixPosition(colorWidget, colPickX, colPickY, applet, PositionAnchor.CENTER);
					        colorWidget.setStrokeColor(MTColor.WHITE);
					        colorWidget.addGestureListener(DragProcessor.class, new IGestureEventListener() {
								public boolean processGestureEvent(MTGestureEvent ge) {
									if (ge.getId()== MTGestureEvent.GESTURE_ENDED){
										colorWidget.setVisible(false);
									}else{
										((AbstractElementView) m_source).setFillColor(colorWidget.getSelectedColor());
									}
									return false;
								}
							});
					        getParent().getParent().getParent().addChild(colorWidget);
					        colorWidget.setVisible(true);
							break;
						case MT4JConstants.CONTEXT_BUTTON_EXPORT_PDF:
							break;
						case MT4JConstants.CONTEXT_BUTTON_HIDE:
							break;
						case MT4JConstants.CONTEXT_BUTTON_SPLIT_GROUP:
							m_source.removeAllChildren();
							m_source.destroy();
							break;
						case MT4JConstants.CONTEXT_BUTTON_EXIT:
							Object[] options = {"Quitter",
							                    "Annuler",};
							ImageIcon icon = new ImageIcon("Image/logo.png");
							Image img = icon.getImage();
							Image newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH );
							icon.setImage(newimg);
							int confirmation = JOptionPane.showOptionDialog(applet,
							    "Etes-vous sûr de vouloir quitter ?",
							    "Quitter WorkHub",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    icon, options, null);
							if (0 == confirmation)
								applet.exit();
							break;
						}
						getParent().getParent().removeFromParent();
						break;
					case AbstractCursorInputEvt.INPUT_ENDED:
						break;
					case AbstractCursorInputEvt.INPUT_UPDATED:
						break;
					default:
						break;
					}
				}
				return false;
			}
		});

	}
}
