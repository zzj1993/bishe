package Martrics;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @Stone6762
 */
public class SparseArray {

	/**
	 * @Fields data : TODO(�������ݵĵط�)
	 */
	private TripleNode data[];
	/**
	 * @Fields rows : TODO(ԭʼ���ݵ�����)
	 */
	private int rows;
	/**
	 * @Fields cols : TODO(ԭʼ���ݵ�����)
	 */
	private int cols;
	/**
	 * @Fields nums : TODO(�ִ����ݵĸ���)
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
	 * @������
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
	 * @������
	 * @param arr
	 */
	public SparseArray(int arr[][]) {
		this.rows = arr.length;
		this.cols = arr[0].length;
		// ͳ���ж��ٷ���Ԫ�أ��Ա�������ռ������
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] != 0) {
					nums++;
				}
			}
		}
		// ��������ͳ�Ƶķ������ݵĸ�������ÿһ������Ԫ�ص���Ϣ���б���
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
	 * @Description: TODO(��ӡ������ϡ�����)
	 */
	public void printArrayOfRC() {
		System.out.println("ϡ��������Ԫ�鴢��ṹΪ��  ");
		System.out.println("����" + rows + ", ����Ϊ��" + cols + " ,����Ԫ�ظ���Ϊ��  "
				+ nums);
		System.out.println("���±�           ���±�         Ԫ��ֵ     ");
		for (int i = 0; i < nums; i++) {
			System.out.println(" " + data[i].getRow() + "      "
					+ data[i].getColumn() + "     " + data[i].getValue());
		}
	}

	/**
	 * @Description: TODO( )
	 */
//	public void printArr() {
//		System.out.println("ϡ��������Ԫ�鴢��ṹΪ��  ");
//		System.out.println("����" + rows + ", ����Ϊ��" + cols + " ,����Ԫ�ظ���Ϊ��  "
//				+ nums);
//		double origArr[][] = reBackToArr();
//		ArrayUtils.printMulArray(origArr);
//		
//	}

	/**
	 * @Description: TODO(��ϡ�����ԭ��Ӱ�Ӿ��� )
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

