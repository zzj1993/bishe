package Martrics;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @Stone6762
 */
public class SparseArray {

	/**
	 * @Fields data : TODO(储存数据的地方)
	 */
	private TripleNode data[];
	/**
	 * @Fields rows : TODO(原始数据的行数)
	 */
	private int rows;
	/**
	 * @Fields cols : TODO(原始数据的列数)
	 */
	private int cols;
	/**
	 * @Fields nums : TODO(现存数据的个数)
	 */
	private int nums;

	public TripleNode[] getData() {
		return data;
	}

	public void setData(TripleNode[] data) {
		this.data = data;
		this.nums = data.length;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public SparseArray() {
		super();
	}

	/**
	 * @构造器
	 * @param maxSize
	 */
	public SparseArray(int maxSize) {
		data = new TripleNode[maxSize];
		for (int i = 0; i < data.length; i++) {
			data[i] = new TripleNode();
		}
		rows = 0;
		cols = 0;
		nums = 0;
	}

	/**
	 * @构造器
	 * @param arr
	 */
	public SparseArray(int arr[][]) {
		this.rows = arr.length;
		this.cols = arr[0].length;
		// 统计有多少非零元素，以便于下面空间的申请
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] != 0) {
					nums++;
				}
			}
		}
		// 根据上面统计的非零数据的个数，将每一个非零元素的信息进行保存
		data = new TripleNode[nums];
		for (int i = 0, k = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] != 0) {
					data[k] = new TripleNode(i, j, arr[i][j]);
					k++;
				}
			}
		}
	}

	/**
	 * @Title: printArray
	 * @Description: TODO(打印储存后的稀疏矩阵)
	 */
	public void printArrayOfRC() {
		System.out.println("稀疏矩阵的三元组储存结构为：  ");
		System.out.println("行数" + rows + ", 列数为：" + cols + " ,非零元素个数为：  "
				+ nums);
		System.out.println("行下标           列下标         元素值     ");
		for (int i = 0; i < nums; i++) {
			System.out.println(" " + data[i].getRow() + "      "
					+ data[i].getColumn() + "     " + data[i].getValue());
		}
	}

	/**
	 * @Description: TODO( )
	 */
//	public void printArr() {
//		System.out.println("稀疏矩阵的三元组储存结构为：  ");
//		System.out.println("行数" + rows + ", 列数为：" + cols + " ,非零元素个数为：  "
//				+ nums);
//		double origArr[][] = reBackToArr();
//		ArrayUtils.printMulArray(origArr);
//		
//	}

	/**
	 * @Description: TODO(将稀疏矩阵还原成影视矩阵 )
	 * @return
	 */
	public double[][] reBackToArr() {
		double arr[][] = new double[rows][cols];
		for (int i = 0; i < nums; i++) {
			arr[data[i].getRow()][data[i].getColumn()] = data[i].getValue();
		}
		return arr;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		dataSQL.loadFile lf = new dataSQL.loadFile();
		int prefer1[][] = lf.loadMovieLensTrain();
		
		SparseArray sa = new SparseArray(prefer1);
//		sa.printArrayOfRC();
		
		System.out.println(sa.getRows()+" "+sa.getCols()+" "+sa.getNums());
		TripleNode data[] = sa.getData();
//		for(int i=0;i<data.length;i++){
//			System.out.println(data[i].getRow()+" "+data[i].getColumn()+" "+data[i].getValue());
//		}
		System.out.println(data[0].getRow()+" "+data[0].getColumn()+" "+data[0].getValue());
	}
}

