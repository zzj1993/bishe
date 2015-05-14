package Martrics;

/**
 * @Stone6762
 */
public class TripleNode {

	/**
	 * @Fields row : TODO(该元素所在的行)
	 */
	private int row;

	/**
	 * @Fields column : TODO(该元素所在的列)
	 */

	private int column;

	/**
	 * @Fields value : TODO(该位置所储存的内容)
	 */
	private double value;

	/**
	 * @构造器
	 * @param row
	 * @param column
	 * @param value
	 */
	public TripleNode(int row, int column, double value) {
		super();
		this.row = row;
		this.column = column;
		this.value = value;
	}

	/**
	 * @构造器
	 */
	public TripleNode() {
		this(0, 0, 0);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[ (" + row + "," + column + "), "
				+ value + " ]";
	}

}
