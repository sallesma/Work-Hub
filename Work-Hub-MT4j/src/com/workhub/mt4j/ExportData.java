package com.workhub.mt4j;

import org.mt4j.components.MTComponent;
import org.mt4j.util.math.Vector3D;

public class ExportData {
	private MTComponent component;
	private Vector3D location;
	
	public ExportData(MTComponent component, Vector3D location) {
		this.component = component;
		this.location = location;
	}

	public MTComponent getComponent() {
		return component;
	}

	public Vector3D getLocation() {
		return location;
	}
}
