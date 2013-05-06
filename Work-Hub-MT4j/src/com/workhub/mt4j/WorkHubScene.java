package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class WorkHubScene extends AbstractScene {
	private WorkHubButton menuButton;
	private WorkHubButton envoyerButton;
	private WorkHubButton recevoirButton;
	private WorkHubButton supprimerButton;
	
	public WorkHubScene(MTApplication mtApplication, String name) throws WorkHubException{
		super(mtApplication, name);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		menuButton = new WorkHubButton("Menu", Constants.CORNER_TOP_LEFT, 130, 1000, 40, 40, getMTApplication());
		envoyerButton = new WorkHubButton("Envoyer", Constants.CORNER_BOTTOM_RIGHT, 130, 1000, 980, 700, getMTApplication());
		recevoirButton = new WorkHubButton("Recevoir", Constants.CORNER_BOTTOM_LEFT, 130, 1000, 50, 700, getMTApplication());
		supprimerButton = new WorkHubButton("Supprimer", Constants.CORNER_TOP_RIGHT, 130, 1000, 980, 40, getMTApplication());
		supprimerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth()-20, -20));
		this.getCanvas().addChild(menuButton);
		this.getCanvas().addChild(supprimerButton);
		this.getCanvas().addChild(envoyerButton);
		this.getCanvas().addChild(recevoirButton);
		
		addElementView(Constants.ELEMENT_TEXT);
		addElementView(Constants.ELEMENT_IMAGE);
		addElementView(Constants.ELEMENT_LINK);
		addElementView(Constants.ELEMENT_FILE);
	}

	@Override
	public void init() {
	}

	@Override
	public void shutDown() {
	}

	public AbstractElementView addElementView(Integer elementId)
			throws WorkHubException {
		switch (elementId) {
		case Constants.ELEMENT_TEXT:
			TextElementView textElement = new TextElementView(200, 200, 0, 200,200, getMTApplication());
			this.getCanvas().addChild(textElement);
			break;
		case Constants.ELEMENT_LINK:
			LinkElementView linkElement = new LinkElementView(200, 200, 0, 200,200, getMTApplication());
			this.getCanvas().addChild(linkElement);
			break;
		case Constants.ELEMENT_IMAGE:
			ImageElementView imageElement = new ImageElementView(200, 200, 0, 200, 200, getMTApplication());
			this.getCanvas().addChild(imageElement);
			break;
		case Constants.ELEMENT_FILE:
			FileElementView fileElement = new FileElementView(200, 200, 0, 200, 200, getMTApplication());
			this.getCanvas().addChild(fileElement);
			break;
		default:
			throw new WorkHubException("Type d'élément invalide.");
		}
		return null;

	}
}
