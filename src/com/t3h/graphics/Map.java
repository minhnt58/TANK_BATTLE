package com.t3h.graphics;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;

public class Map {
	public Commons common = new Commons();
	
	private File file;
	private RandomAccessFile rdf;
	
	private int matrix[][];		// Luu trang thai cua ban do
	
	public Map(int type) {		
		createMatrix();
		String tempFilePath = getClass().getResource("/RESOURCE/Map").getPath();
		String pathMap = tempFilePath + "/map" + type;
		System.out.println(pathMap);
		file = new File(pathMap);
		openFile();
		readFile();
		closeFile();
	}
	
	// Ve nhung thanh phan o phia duoi ma bullet duoc ve de len tren no
	public void drawUnderComponent(Graphics2D g2d){
		int type;
		for (int i=0; i<Commons.sizeMap; i++){
			for (int j=0; j<Commons.sizeMap; j++){
				type = matrix[i][j];
				switch(type){
					case Commons.RIM:{
						g2d.drawImage(Commons.rim, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, null);
						break;
					}
					case Commons.BRICK1:{
						g2d.drawImage(Commons.brick1, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, null);
						break;
					}
					case Commons.STONE:{
						g2d.drawImage(Commons.stone, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, null);
						break;
					}
					case Commons.BRICK2:{
						g2d.drawImage(Commons.brick2, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, null);
						break;
					}
					case Commons.WATER:{
						g2d.drawImage(Commons.water, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT,null);
						break;
					}
					default:{
						break;
					}
				}
			}
		}
	}
	
	// Ve nhung thanh phan duoc ve de len tren bullet
	public void drawComponent(Graphics2D g2d){
		int type;
		for (int i=1; i<Commons.sizeMap-1; i++){
			for (int j=1; j<Commons.sizeMap-1; j++){
				type = matrix[i][j];
				switch(type){
					case Commons.NONE:{
						break;
					}
					case Commons.TREE:{
						g2d.drawImage(Commons.tree, i*Commons.SIZE_COMPONENT, j*Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT, Commons.SIZE_COMPONENT,null);
						break;
					}
					default:{	
						break;
					}
				}
			}
		}
	}
	
	//-----------------------------FILE---------------------
	private void openFile(){
		try {
			if (file.exists()){
				rdf = new RandomAccessFile(file, "rw");
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy Map");
		}
	}
	private void closeFile(){
		try {
			rdf.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Không thể đóng file Map");
		}
	}
	private long filePointer(){
		try {
			return rdf.getFilePointer();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Không thể đọc File");
		}
		return 0;
	}
	private String readLine(){
		try {
			return rdf.readLine();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Không thể đọc File");
		}
		return "";
	}
	
	
	private void readFile(){
		long lenFile = file.length();
		String line = "";
		int pos, i, j, type;
		while (filePointer()<lenFile){
			line = readLine();
			pos = line.indexOf(" ");
			i = Integer.parseInt(line.substring(0, pos));
			j = Integer.parseInt(line.substring(pos+1, line.length()));
			type = Integer.parseInt(readLine());
			matrix[i+1][j+1] = type;
		}
	}
	
	private void createMatrix(){
		matrix = new int [Commons.sizeMap][Commons.sizeMap];
		for (int i = 0; i < Commons.sizeMap; i++) {
			matrix[0][i]	= matrix[i][0]
							= matrix[Commons.sizeMap - 1][i]
							= matrix[i][Commons.sizeMap - 1]
							= Commons.RIM;	// Vien bao cua Map
		}
	}
	
	public void setMatrix(int x, int y, int type) {
		matrix[x/Commons.SIZE_COMPONENT][y/Commons.SIZE_COMPONENT] = type;
	}
	
	public int getType(int xScreen, int yScreen){
		return matrix[xScreen/Commons.SIZE_COMPONENT][yScreen/Commons.SIZE_COMPONENT];
	}
}

