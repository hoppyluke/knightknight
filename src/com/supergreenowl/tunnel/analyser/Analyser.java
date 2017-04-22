package com.supergreenowl.tunnel.analyser;

public class Analyser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.println("Usage: Analyser WEST EAST");
			return;
		}
		
		String w = args[0];
		String e = args[1];
		
		System.out.println("Analysing " + w + " vs. " + e + ":");
		
		AnalysisEngine analysis = new AnalysisEngine();
		analysis.analyse(w, e);
		
		double westPercent = ((double)analysis.west / (double)analysis.combinations) * 100d;
		double eastPercent = ((double)analysis.east / (double)analysis.combinations) * 100d;
		double drawPercent = ((double)analysis.draws / (double)analysis.combinations) * 100d;
		
		System.out.printf("Combinations: %,d (%,d x %,d)", analysis.combinations, analysis.westCombinations, analysis.eastCombinations);
		System.out.println();
		System.out.printf("West wins: %,d / %,d (%.2f%%)", analysis.west, analysis.combinations, westPercent);
		System.out.println();
		System.out.printf("East wins: %,d / %,d (%.2f%%)", analysis.east, analysis.combinations, eastPercent);
		System.out.println();
		System.out.printf("Draws: %,d / %,d (%.2f%%)", analysis.draws, analysis.combinations, drawPercent);
		System.out.println();
		
		if(analysis.west == analysis.east) {
			System.out.println("Strategies are equal.");
		}
		else {
			System.out.printf("%s is the better strategy", analysis.west > analysis.east ? w : e);
		}

		
		
		
	}

}
