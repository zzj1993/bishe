package Martrics;

/**
 * @Stone6762
 */
public class TripleNode {

	/**
	 * @Fields row : TODO(��Ԫ�����ڵ���)
	 */
	private int row;

	/**
	 * @Fields column : TODO(��Ԫ�����ڵ���)
	 */

	private int column;

	/**
	 * @Fields value : TODO(��λ�������������)
	 */
	private double value;

	/**
	 * @������
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
	 * @������
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
