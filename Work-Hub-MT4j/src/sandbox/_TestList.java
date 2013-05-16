package sandbox;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

public class _TestList extends AbstractScene {
	MTApplication m_app;
	
	public _TestList(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		m_app = mtApplication;
	}

	@Override
	public void init() {
		MTList list = new MTList(100, 100, 200, 200, m_app);		
		getCanvas().addChild(list);
		list.setFillColor(MTColor.BLUE);
		
		IFont font = FontManager.getInstance().createFont(m_app, "arial.ttf", 24);
		
		MTListCell item1 = new MTListCell(200, 50, m_app);
		MTTextField text1 = new MTTextField(0, 0, 200, 50, font, m_app);
		text1.setFillColor(MTColor.AQUA);
		text1.setText("Item 1");
		item1.addChild(text1);
		list.addListElement(item1);
		
		MTListCell item2 = new MTListCell(200, 50, m_app);
		MTTextField text2 = new MTTextField(0, 0, 200, 50, font, m_app);
		text2.setFillColor(MTColor.AQUA);
		text2.setText("Item 2");
		item2.addChild(text2);
		list.addListElement(item2);
		
		MTListCell item3 = new MTListCell(200, 50, m_app);
		MTTextField text3 = new MTTextField(0, 0, 200, 50, font, m_app);
		text3.setFillColor(MTColor.AQUA);
		text3.setText("Item 3");
		item3.addChild(text3);
		list.addListElement(item3);
		
		MTListCell item4 = new MTListCell(200, 50, m_app);
		MTTextField text4 = new MTTextField(0, 0, 200, 50, font, m_app);
		text4.setFillColor(MTColor.AQUA);
		text4.setText("Item 4");
		item4.addChild(text4);
		list.addListElement(item4);
		
		list.setVisible(true);
	}

	@Override
	public void shutDown() {
	}
}
