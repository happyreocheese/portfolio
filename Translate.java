package chapSVM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;
public class Translate {

	public static void main(String[] args) throws Exception{
		// TODO 自動生成されたメソッド・スタブ
		//なぜかdirでおくと出来なくなる。
		/*
		String dir ="C:/pleiades/2022-09/workspace/領域実習A\\\\src/chapSVM/";
		
		String inputFileName = "iris.data.kwansei.txt";
		String output_trainFileName = "output_train.txt";
		String output_testFileName = "output_test.txt";
		BufferedReader br = new BufferedReader(new FileReader(dir + inputFileName));
		BufferedWriter bw_train = new BufferedWriter(new FileWriter(dir + output_trainFileName));
		BufferedWriter bw_test = new BufferedWriter(new FileWriter(dir + output_testFileName));
		*/
		BufferedWriter bw;
		
		String inputFileName="C:/pleiades/2022-09/workspace/領域実習A\\\\src/chapSVM/iris.data.kwansei.txt";
		String output_trainFileName="C:/pleiades/2022-09/workspace/領域実習A\\src/chapSVM/output_train.txt";
		String output_testFileName="C:/pleiades/2022-09/workspace/領域実習A\\src/chapSVM/output_test.txt";
		BufferedReader br=new BufferedReader(new FileReader(inputFileName));
		BufferedWriter bw_train=new BufferedWriter(new FileWriter(output_trainFileName));
		BufferedWriter bw_test=new BufferedWriter(new FileWriter(output_testFileName));
		
		
		Random random=new Random();
		String buffer;
		while((buffer=br.readLine())!=null) {
			//ベクトル化
			String[]parts=buffer.split(",");
			//ここで分類
			String label = parts[4].trim();
			if (label.equals("Iris-setosa")) {
                 label = "+1";
              } else {
                 label = "-1";
              }
			int random_num=random.nextInt(10);//ここでだいたい9:1にわけている
			if (random_num == 2) {
				bw = bw_test;
			} else {
				bw = bw_train;
			}
			bw.write(label + " " + " " + "1:" + parts[0] + " " + "2:" + parts[1] + " " + "3:" + parts[2] + " " + "4:"
					+ parts[3] + "\n");
			
			
		
		}
	
		
		br.close();
		bw_train.close();
		bw_test.close();
		
		// training
        String trainCommand = "C:/pleiades/2022-09/workspace/領域実習A/src/chapSVM/svm-train " + output_trainFileName + " model.txt";
        executeCommand(trainCommand);

        // test
        String testCommand = "C:/pleiades/2022-09/workspace/領域実習A/src/chapSVM/svm-predict " + output_testFileName + " model.txt output.txt";
        executeCommand(testCommand);
		
		
		
		

	}
	
	  public static void executeCommand(String command) throws Exception {
	        Process process = Runtime.getRuntime().exec(command);//ここでコマンドを実行する
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));//バイトからjavaが理解できる文字列に変換
	        String line;
	        while ((line = reader.readLine()) != null) {//１行ずつよむ
	            System.out.println(line);
	        }
	        reader.close();
	        
	    }
	    
	
	
	

}
