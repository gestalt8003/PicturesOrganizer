package com.gestalt8003.picturesorganizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

public class Picture {
	
	private File file;
	private int width;
	private int height;

	public Picture(File file) {
		this.file = file;
		int[] dim = getPictureDimensions(file);
		setWidth(dim[0]);
		setHeight(dim[1]);
	}
	
	public boolean isAspectRatio(int[] aspectRatio) {
		if(aspectRatio.length != 2) {
			return false;
		}
		return width/aspectRatio[0]*aspectRatio[1] == height;
	}
	
	public boolean isSize(int width, int height) {
		return this.width == width && this.height == height;
	}

	@SuppressWarnings("resource")
	public void copyTo(File dir) {
		File newFile = new File(dir.getAbsolutePath() + File.separator + file.getName());
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(file).getChannel();
			outputChannel = new FileOutputStream(newFile).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
				// Delete original file afterwards
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int[] getPictureDimensions(File file) {
		int[] dim = new int[2];
		try {
			BufferedImage bimage = ImageIO.read(file);
			dim[0] = bimage.getWidth();
			dim[1] = bimage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dim;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
}
