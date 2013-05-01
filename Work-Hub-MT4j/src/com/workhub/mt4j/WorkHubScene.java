package com.workhub.mt4j;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

public class WorkHubScene extends AbstractScene {
	private WorkHubButton menuButton = new WorkHubButton("Menu", 0, 0, 0, 260, 200, 30, 50, 1000, getMTApplication());
	private WorkHubButton envoyerButton = new WorkHubButton("Envoyer", 0, 0, 0, 200, 260, 30, 50, 1000, getMTApplication());
	private WorkHubButton recevoirButton = new WorkHubButton("Recevoir", 0, 0, 0, 200, 260, 30, 50, 1000, getMTApplication());
	private WorkHubButton supprimerButton = new WorkHubButton("Supprimer", 0, 0, 0, 260, 200, 30, 50, 1000, getMTApplication());
	
	public WorkHubScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));

		menuButton.setTextPosition(new Vector3D(menuButton.getHeightXY(TransformSpace.LOCAL)-20, menuButton.getHeightXY(TransformSpace.LOCAL)-20));
		menuButton.setPositionGlobal(new Vector3D(-10, -60));
		envoyerButton.setTextPosition(new Vector3D(60, 20));
		envoyerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth(), mtApplication.getHeight()+60));
		recevoirButton.setTextPosition(new Vector3D(recevoirButton.getHeightXY(TransformSpace.LOCAL)-110, 20));
		recevoirButton.setPositionGlobal(new Vector3D(0, mtApplication.getHeight()+60));
		supprimerButton.setTextPosition(new Vector3D(70, supprimerButton.getHeightXY(TransformSpace.LOCAL)-20));
		supprimerButton.setPositionGlobal(new Vector3D(mtApplication.getWidth(), -60));
		this.getCanvas().addChild(menuButton);
		this.getCanvas().addChild(supprimerButton);
		this.getCanvas().addChild(envoyerButton);
		this.getCanvas().addChild(recevoirButton);
		
		TextElementView textElement = new TextElementView(getMTApplication(), new Vertex[]{
			new Vertex(200, 200),
			new Vertex(200, 400),
			new Vertex(400, 400),
			new Vertex(400, 200)
		});
		textElement.setPositionGlobal(new Vector3D(200, 200));
		this.getCanvas().addChild(textElement);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void shutDown() {
		
	}

}
