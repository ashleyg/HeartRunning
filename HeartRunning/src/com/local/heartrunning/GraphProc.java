package com.local.heartrunning;

import java.util.ArrayList;

public class GraphProc {
	int area = 5;
	
	
	ArrayList<Integer> data;
	ArrayList<Integer> smoothedData;
	
	public GraphProc() {
		data = new ArrayList<Integer>();
		smoothedData = new ArrayList<Integer>();
	}
	
	public void addDataPoint(int level) {
		data.add(Integer.valueOf(level));
	}
	
	public void updateSmoothedData() {
		if(data.size() >= area) {
			int total = 0;
			for(int i=data.size()-area;i<data.size();i++) {
				total += data.get(i);
			}
			total = total/area;
			
			smoothedData.add(Integer.valueOf(total));
		}
	}
	
	public ArrayList<Integer> findPeaks() {
		ArrayList<Integer> peaks = new ArrayList<Integer>();
		
		for(int i=0;i<data.size();i++) {
			//Ensure that we don't underflow/overflow the array
			if(i > 0 && i < data.size()) {
				if(data.get(i-1) > data.get(i) && data.get(i+1) < data.get(i)) {
					peaks.add(Integer.valueOf(i));
				}
			}
		}
		
		return peaks;
	}
	
	
}
