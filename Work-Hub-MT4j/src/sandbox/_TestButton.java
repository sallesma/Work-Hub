package sandbox;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

public class _TestButton extends AbstractScene {
	private MTTextArea m_myButton;

	public _TestButton(MTApplication app, String name) {
		super(app, name);

		this.setClearColor(new MTColor(213, 187, 255, 255));
		m_myButton = new MTTextArea(400, 300, 300, 100, app);
		m_myButton.setFillColor(new MTColor(247, 179, 53, 255));
		m_myButton.setText("Click me");

		// Empêche le bouton d'être déplacé
		m_myButton.unregisterAllInputProcessors();
		m_myButton.removeAllGestureEventListeners();

		m_myButton.addInputListener(new IMTInputEventListener() {

			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
					switch (cursorInputEvt.getId()) {
					case AbstractCursorInputEvt.INPUT_DETECTED:
						m_myButton.setFillColor(MTColor.RED);
						break;
					case AbstractCursorInputEvt.INPUT_ENDED:
						m_myButton.setFillColor(new MTColor(247, 179, 53, 255));
						break;
					default:
						break;
					}
				}
				return false;
			}
		});

		m_myButton.setVisible(true);
		getCanvas().addChild(m_myButton);
	}

	@Override
	public void init() {
		System.out.println("TestButtonScene ready");
	}

	@Override
	public void shutDown() {
		// Do nothing
	}
}
