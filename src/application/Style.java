package application;

import java.util.HashSet;
import java.util.Set;

public class Style {
	
	String style;
	Set<String> colorSet;
	
	public Style(String style) {
		this.style = style;
		this.colorSet = new HashSet<String>();
	}
	
	public String getStyle() {
		return this.style;
	}
	
	public Set<String> getColorSet(){
		return this.colorSet;
	}
	
	public void addColor(String color) {
		this.colorSet.add(color);
	}
	
}
