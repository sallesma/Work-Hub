package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class WorkHubScene extends AbstractScene {
	private WorkHubButton menuButton = new WorkHubButton("Menu", 0, 0, 0, 260, 260, 130, 130, 1000, getMTApplication());
	private WorkHubButton envoyerButton = new WorkHubButton("Envoyer", 0, 0, 0, 260, 260, 130, 130, 1000, getMTApplication());
	private WorkHubButton recevoirButton = new WorkHubButton("Recevoir", 0, 0, 0, 260, 260, 130, 130, 1000, getMTApplication());
	private WorkHubButton supprimerButton = new WorkHubButton("Supprimer", 0, 0, 0, 260, 260, 130, 130, 1000, getMTApplication());

	public static final int TEXT_ELEMENT 	= 0;
	public static final int LINK_ELEMENT 	= 1;
	public static final int IMAGE_ELEMENT 	= 2;
	public static final int FILE_ELEMENT 	= 3;
	
	public WorkHubScene(MTApplication mtApplication, String name) throws WorkHubException{
		super(mtApplication, name);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));

		
		menuButton.setTextPosition(new Vector3D(menuButton.getHeightXY(TransformSpace.LOCAL)-70, menuButton.getHeightXY(TransformSpace.LOCAL)-80));
		menuButton.setPositionGlobal(new Vector3D(-20, -20));
		envoyerButton.setTextPosition(new Vector3D(80, 70));
		envoyerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth(), mtApplication.getHeight()-20));
		recevoirButton.setTextPosition(new Vector3D(recevoirButton.getHeightXY(TransformSpace.LOCAL)-70, 70));
		recevoirButton.setPositionGlobal(new Vector3D(0, mtApplication.getHeight()-20));
		supprimerButton.setTextPosition(new Vector3D(90, supprimerButton.getHeightXY(TransformSpace.LOCAL)-80));
		supprimerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth()-20, -20));
		this.getCanvas().addChild(menuButton);
		this.getCanvas().addChild(supprimerButton);
		this.getCanvas().addChild(envoyerButton);
		this.getCanvas().addChild(recevoirButton);
		
		addElementView(TEXT_ELEMENT);
		addElementView(IMAGE_ELEMENT);
		addElementView(LINK_ELEMENT);
		addElementView(FILE_ELEMENT);
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
		case TEXT_ELEMENT:
			TextElementView textElement = new TextElementView(200, 200, 0, 200,200, getMTApplication());
			this.getCanvas().addChild(textElement);
			break;
		case LINK_ELEMENT:
			LinkElementView linkElement = new LinkElementView(200, 200, 0, 200,200, getMTApplication());
			this.getCanvas().addChild(linkElement);
			break;
		case IMAGE_ELEMENT:
			ImageElementView imageElement = new ImageElementView(200, 200, 0,200, 200, getMTApplication());
			this.getCanvas().addChild(imageElement);
			break;
		case FILE_ELEMENT:
			FileElementView fileElement = new FileElementView(200, 200, 0, 200,200, getMTApplication());
			this.getCanvas().addChild(fileElement);
			break;
		default:
			throw new WorkHubException("Type d'élément invalide.");
		}
		return null;

	}
}
