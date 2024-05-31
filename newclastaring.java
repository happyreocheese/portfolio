package chapClasstaring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

public class newclastaring {

	public static void main(String[] args) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

		// 1データをファイルから読み取りメモリ上に保存

		String dir = "C:/pleiades/2022-09/workspace/領域実習A/src/chapClasstaring/";
		String inputFileName = "iris.data.kwansei.txt";
		BufferedReader br = new BufferedReader(new FileReader(dir + inputFileName));// 読み込みファイルを開く
		String line;
		Vector<double[]> samples_data = new Vector<>();// データをここに保存する
		int [] id=new int [100];

		int ID = -1;

		while ((line = br.readLine()) != null) {// １行ずつ読み込む
			ID++;
			String[] parts = line.split(",");// コンマで区切る,データをわける
			double[] sample_data = new double[parts.length-1];// 最後のラベルは取り除く
			for (int i = 0; i < parts.length - 1; i++) {
				sample_data[i] = Double.parseDouble(parts[i]);// double型に変換

			}
			// 2各点に対してクラスタIDをつける
			//sample_data[parts.length - 1] = ID;
			samples_data.add(sample_data);// Vectorで追加する
			id[ID]=ID;

		}
		br.close();

		// 3任意の２点間の距離を求める。
		for (int n = 1; n <= samples_data.size() - 2; n++) {
			double mindis = Double.MAX_VALUE;// doubleの中の最大値
			int min_i = -1;
			int min_j = -1;

			for (int i = 0; i < samples_data.size(); i++) {
				for (int j = i + 1; j < samples_data.size(); j++) {//
					double[] sample_a = samples_data.get(i);// 中の特徴量を一つ取り出すために
					double[] sample_b = samples_data.get(j);//
					double sum_dis = 0;
					for (int h = 0; h < sample_a.length; h++) {
						double per_dis = (sample_a[h] - sample_b[h]) * (sample_a[h] - sample_b[h]);// 各自の距離
						sum_dis += per_dis;
					}
					double dis = Math.sqrt(sum_dis);// ここでsample同士の距離
					// 4距離が最小の２点i,jを求める
					if (mindis > dis && id[i] != id[j]) {// 同じIDならはじく

						//System.out.println("sample_a;" + id[i] + "sample_b:"+ id[j]);
						//System.out.println("i:" + i + " j:" + j);
						mindis = dis;
						min_i = i;
						min_j = j;

					}

				}

			}
			// 5 jと同じクラスタIDを持つ点のクラスタIDをiのクラスタIDに変換する
			if (min_i != -1 && min_j != -1) {
				int newID=id[min_i];
				int oldID=id[min_j];
				for(int k=0;k<id.length;k++) {
					if (id[k] == oldID) {
                        id[k] = newID;
                    }
				}
				
				System.out.println(n+"\t"+numOfClusters(id)+"\t"+Arrays.toString(id));

			} else {

				System.out.println("エラーだよ");
			}

		}
		// データを表示
		for (int i = 0; i < samples_data.size(); i++) {
			double[] sample = samples_data.get(i);
			for (int j = 0; j < sample.length; j++) {
				System.out.print(sample[j] + "//");
			}
			System.out.print("ID="+id[i]);
			System.out.print("\n");
		}

	}
	
	static int numOfClusters(int [] id) {
		HashSet<Integer> set=new HashSet<Integer>();
		for(int i=0;i<id.length;i++) {
			set.add(id[i]);
		}
		
		return set.size();
	}

}