import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Viterbi {
	Viterbi(){
		
	}
	
	public static void main(String args[])throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str = "";
		System.out.println("enter the observation sequence without space between the observed characters eg 331 ");
		str = br.readLine();
		String[] sentenceWords = new String[str.length()];
		
		for(int i=0; i<str.length(); i++){
			char ch = str.charAt(i); 
			sentenceWords[i]= ch + "";
		}
		/*for(int i=0; i<str.length(); i++){
			System.out.print(sentenceWords[i]);
		}*/
		
		System.out.println();
		
		String transitionProb[][] = new String[3][3];
		String wordObsProb[][] = new String[3][4];
		String viterbi[][] = new String[3][sentenceWords.length + 1];
		String backTrack[][] = new String[3][sentenceWords.length + 1];
		
		Viterbi vb = new Viterbi();
		vb.initilaize(transitionProb, wordObsProb, viterbi, backTrack, sentenceWords);
		
		System.out.println("transition probabilities");
		for(int row = 0; row<3; row++){
			for(int col=0; col<3; col++){
				System.out.format("%16s",transitionProb[row][col]);
			}
			System.out.println();
		}
		System.out.println("\nstate observation probabilities");
		for(int row = 0; row<3; row++){
			for(int col=0; col<4; col++){
				System.out.format("%16s",wordObsProb[row][col]);
			}
			System.out.println();
		}
		
		vb.calculate(transitionProb,wordObsProb, viterbi, backTrack, sentenceWords );
		
		
	}

	private void calculate(String[][] transitionProb,
			String[][] wordObsProb, String[][] viterbi, String[][] backTrack, String[] sentenceWords) {
		// TODO Auto-generated method stub
		double prior[] ={0.8, 0.2};
		for(int row=1; row<= 2; row++){
			viterbi[row][1] = String.valueOf(prior[row-1] * Double.parseDouble(wordObsProb[row][(Integer.parseInt(viterbi[0][1]))]));
			backTrack[row][1] = "0";
		}
		
	/*	for(int row = 0; row<3; row++){
			for(int col=0; col<= sentenceWords.length; col++){
				System.out.format("%32s",viterbi[row][col]);
			}
			System.out.println();
		}
		*/
		for(int col=2; col<= sentenceWords.length; col++){
			for(int row=1; row<=2; row++){
				double max = 0.0;
				int index = 0;
				for(int temp=1; temp<=2; temp++){
					double k=Double.parseDouble((wordObsProb[row][(Integer.parseInt(viterbi[0][col]))])) * Double.parseDouble(viterbi[temp][col-1]);
					double trans=0.0;
					trans = Double.parseDouble(transitionProb[temp][row]);
					k = k * trans;
					//if( k > max){
					if((Double.compare(k, max)>0)){
						max = k;
						index = temp;
					}
				}
				viterbi[row][col] = String.valueOf(max);
				backTrack[row][col] = String.valueOf(index);
			}
		}
		
		System.out.println("\nViterbi Matrix");
		for (int row = 0; row <= 2; row++) { // row
			for (int col = 0; col <= sentenceWords.length; col++) {
				System.out.format("%32s",viterbi[row][col]);
			}
			System.out.println();
		}
		System.out.println("\nBacktracking Matrix corresponding to the viterbi matrix");
		for (int row = 0; row <= 2; row++) { // row
			for (int col = 0; col <= sentenceWords.length; col++) {
				System.out.format("%32s",backTrack[row][col]);
			}
			System.out.println();
		}
		
		int ind = 0;
		double max =0.0;
		int lastCol = sentenceWords.length;
		for(int row=1; row<=2; row++){
			if(Double.compare(Double.parseDouble(viterbi[row][lastCol]), max)>0 ){
				max = Double.parseDouble(viterbi[row][lastCol]);
				ind = row;
			}
		}
		
		System.out.println("Probability of the most likely sequence is: "+ max);
		
		String seq[] = new String[sentenceWords.length];
		seq[sentenceWords.length-1] = viterbi[ind][0]; 
		int count = sentenceWords.length-2;
		for(int len = sentenceWords.length;len >1; len--){
			seq[count]= viterbi[Integer.parseInt(backTrack[ind][len])][0];
			 ind = Integer.parseInt(backTrack[ind][len]);
			 count -= 1;	
		}
		String finalAnswer ="";
		for(int j=0; j<seq.length;j++){
			finalAnswer = finalAnswer + seq[j];
		}
		System.out.println("\nThe Most likely sequence after applying Viterbi Algorithm is: ");
		System.out.println(finalAnswer);
		
	}


	private void initilaize(String[][] transitionProb,
			String[][] wordObsProb, String[][] viterbi, String[][] backTrack, String[] sentenceWords) {
		// TODO Auto-generated method stub
		transitionProb[0][0] = "";
		wordObsProb[0][0] = "";
		viterbi[0][0] = "";
		backTrack[0][0] = "";
		
		String states[] = {"H", "C"};
		String observ[] = {"1", "2", "3"};
		
		// initialize headers starts
		for(int i=1; i<3; i++){
			transitionProb[i][0] = states[i-1];
			transitionProb[0][i] = states[i-1];
			wordObsProb[i][0] = states[i-1];
			viterbi[i][0] = states[i-1];
			backTrack[i][0] = states[i-1];
		}
		for(int i=1; i<4; i++){
			wordObsProb[0][i] = observ[i-1];
		}
		for(int i=1; i<= sentenceWords.length; i++){
			viterbi[0][i] = sentenceWords[i-1];
			backTrack[0][i] = sentenceWords[i-1];
		}
		// initialize headers ends
		String transProb[] = {"0.7", "0.3", "0.4", "0.6"};
		String obsState[] = {"0.2", "0.4", "0.4", "0.5", "0.4", "0.1"};
		int count = 0;
		for(int row = 1; row<3; row++){
			for(int col=1; col<3; col++){
				transitionProb[row][col] = transProb[count++];
			}
		}
		count = 0;
		for(int row = 1; row<3; row++){
			for(int col=1; col<4; col++){
				wordObsProb[row][col] = obsState[count++];
			}
		}
		
	}
}