package com.workhub.mt4j;

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
	
	public ContextButton(final PApplet applet, MTComponent source, final String text) {
		super(Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, applet);
		
		IFont font = FontManager.getInstance().createFont(applet, "arial.ttf", 18);
		m_source = source;
		m_text = new MTTextField(0, 0, Constants.CONTEXT_BUTTON_WIDTH, Constants.CONTEXT_BUTTON_HEIGHT, font, applet);
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
						case Constants.CONTEXT_BUTTON_CLOSE:
							//just close the menu (instruction below)
							break;
						case Constants.CONTEXT_BUTTON_DELETE:
							m_source.removeFromParent();
							break;
						case Constants.CONTEXT_BUTTON_CREATE_TEXT:
							TextElementView textElement = new TextElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(textElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_IMAGE:
							String imagePath = applet.selectInput();
							ImageElementView imageElement = new ImageElementView(imagePath, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(imageElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_LINK:
							LinkElementView linkElement = new LinkElementView(((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(linkElement);
							break;
						case Constants.CONTEXT_BUTTON_CREATE_FILE:
							String filePath = applet.selectInput();
							FileElementView fileElement = new FileElementView(filePath, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).x, ((MTRectangle) getParent().getParent()).getPosition(TransformSpace.GLOBAL).y, Constants.ELEMENT_DEFAULT_WIDTH, Constants.ELEMENT_DEFAULT_HEIGHT, applet);
							getParent().getParent().getParent().addChild(fileElement);
							break;
						case Constants.CONTEXT_BUTTON_VISUALIZE_ELEMENTS:
							break;
						case Constants.CONTEXT_BUTTON_EDIT_TITLE:
							((AbstractElementView) m_source).editElementTitle();
							break;
						case Constants.CONTEXT_BUTTON_EDIT_CONTENT:
							((AbstractElementView) m_source).editElementContent();
							break;
						case Constants.CONTEXT_BUTTON_SHARE:
							break;
						case Constants.CONTEXT_BUTTON_CHANGE_COLOR:
							System.out.println("modif color");
							PImage colPick = applet.loadImage("Image/colorcircle.png");
							final MTColorPicker colorWidget = new MTColorPicker(0, 0, colPick, applet);
					        colorWidget.translate(new Vector3D(0f, 135,0));
					        colorWidget.setStrokeColor(new MTColor(0,0,0));
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
						case Constants.CONTEXT_BUTTON_EXPORT_PDF:
							break;
						case Constants.CONTEXT_BUTTON_HIDE:
							break;
						case Constants.CONTEXT_BUTTON_SPLIT_GROUP:
							break;
						case Constants.CONTEXT_BUTTON_EXIT:
							Object[] options = {"Quitter",
							                    "Annuler",};
							int confirmation = JOptionPane.showOptionDialog(applet,
							    "Etes-vous sûr de vouloir quitter ?",
							    "Quitter WorkHub",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null, options, null);
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
