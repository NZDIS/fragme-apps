package nz.org.nzdis.cksw;

import java.util.LinkedList;

public class ToolBox {

	private LinkedList<Tool> tools = new LinkedList<Tool>();
	
	public void addTool(Tool tool) { tools.add(tool); }
	
	/**
	 * Find a tool in the toolbox
	 * @param index The index of the tool
	 * @return The tool from the specified index
	 */
	public Tool getTool(int index) {
		if (tools.size() > index) {
			return tools.get(index);
		} else {
			System.err.println("Agent->getTool: Tried to get a tool which doesn't exist (array out of bounds)");
			return null;
		}
	}
	
	/**
	 * Removes a tool from the toolbox
	 * @param index Index of tool to remove
	 * @return The tool that was removed, or null if index was out-of-bounds
	 */
	public Tool removeTool(int index) {
		if (tools.size() > index) {
			return tools.remove(index);
		} else {
			System.err.println("Agent->removeTool: Tried to remove a tool which doesn't exist (array out of bounds)");
			return null;
		}
	}
	
	/**
	 * Get the number of tools in the toolbox
	 * @return The number of tools in the toolbox
	 */
	public int numberOfTools() { return tools.size(); }

	public boolean containsToolType(int type) {
		for (int i = 0; i < tools.size(); i++) {
			if (tools.get(i).type() == type) {
				return true;
			}
		}
		return false;
	}
}
