package chapItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class item09_13 {

	static BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(System.out));

	// output
	public static void output(ArrayList<Integer> a, String[] int_key) throws Exception {

		for (Integer item : a) {
			bout.write(int_key[item] + " ");
		}
		bout.newLine();

	}

	// min
	public static int min(Set<Integer> x) {
		int min = Integer.MAX_VALUE;
		for (int item : x) {
			if (item < min) {
				min = item;
			}
		}
		return min;
	}



	// .txtからの読み込み
	public static ArrayList<String[]> preserve(String filepath) throws IOException {
		ArrayList<String[]> data = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line;
		while ((line = br.readLine()) != null) {
			// 行をカンマで区切ってString配列に変換
			String[] item = line.split(" ");
			data.add(item);
		}
		br.close();
		/*
		 * // デバッグ用にデータベースの内容を出力 System.out.println("Database content:"); for (String[]
		 * transaction : data) { for (String item : transaction) { System.out.print(item
		 * + " "); } System.out.println(); }
		 */

		return data;
	}

	// tail(X)
	public static int tail(ArrayList<Integer> x) {
		if (x.isEmpty()) {// xが空集合だったら

			return -1;
		} else {
			return x.get(x.size()-1);
		}
	}

	// 2
	public static void mine(ArrayList<Integer> x, ArrayList<int[]> DB, int numItems, String[]  int_key,
			int threthold, int[] preCount) throws Exception {
		output(x, int_key);

		int tail = tail(x);
		int[] count = new int[numItems];
		ArrayList<int[]>[] newDBs = new ArrayList[numItems];
		for (int i = tail + 1; i < numItems; i++) {
			if (preCount == null || preCount[i] >= threthold)
				newDBs[i] = new ArrayList<>();
		}

		for (int[] t : DB) {
			Set<Integer> transactionSet = new HashSet<>();
			for (int item : t) {
				if (newDBs[item] != null) {
					count[item]++;
					newDBs[item].add(t);
				}
			}
		}

		for (int i = tail(x) + 1; i < numItems; i++) { // for each i > tail(x) do


			// System.out.println("sup_x: " + sup_x); // デバッグ用
			if (count[i] >= threthold) {
				x.add(i);
				mine(x, newDBs[i], numItems, int_key, threthold, count);
				x.remove(x.size()-1);

			}
		}
	}

	// アイテムを集合させる
	public static Set<String> Items(ArrayList<String[]> DB) {
		Set<String> p = new HashSet<>();
		for (String[] transaction : DB) {
			for (String item : transaction) {
				p.add(item);
			}
		}
		return p;

	}

	// 文字列からIntegerに変える
	public static ArrayList<int[]> String_to_Integer(ArrayList<String[]> DB, Hashtable<String, Integer> String_key) {
		ArrayList<int[]> intDB = new ArrayList<>();
		for (String[] transaction : DB) {
			int[] intTransaction = new int[transaction.length];
			for (int i = 0; i < transaction.length; i++) {
				intTransaction[i] = String_key.get(transaction[i]);
			}
			intDB.add(intTransaction);

		}
		return intDB;

	}

	public static void main(String[] args) throws Exception {
		// 1データファイルを取得

		long start = System.nanoTime();

		String dir = "C:/pleiades/2022-09/workspace/領域実習A/src/chapItem/";
		String inputFileName = "T10I4D100K.dat.txt";
		ArrayList<String[]> DB = preserve(dir + inputFileName);// データをここに保存する
		Set<String> uniqueItems = Items(DB);// アイテム集合

		int threthold = 2;

		// ハッシュで数字と物をナンバリング
		String[] int_key = new String [uniqueItems.size()];
		Hashtable<String, Integer> String_key = new Hashtable<>();
		int index = 0;
		for (String item : uniqueItems) {
			int_key[index]= item;
			String_key.put(item, index);
			index++;
		}
		// 表示
		for (String item : uniqueItems) {
			System.err.println(item + ":" + String_key.get(item));

		}
		// DBをint型に変換する
		ArrayList<int[]> intDB = String_to_Integer(DB, String_key);
		// 2アルゴリズム
		ArrayList<Integer> initial_items_set = new ArrayList<>();// 初期のアイテム集合（空集合からスタート）
		mine(initial_items_set, intDB, uniqueItems.size(), int_key, threthold, null);
		bout.close();

		long time = System.nanoTime() - start;
		System.err.println(time / 1000 / 1000 / 1000 + " sec");
	}
}