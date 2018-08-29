package com.gestalt8003.picturesorganizer;

import java.io.File;

public class PicturesOrganizer {
	
	public static int[][] WALLPAPER_ASPECT_RATIOS = {{16, 9}, {4, 3}, {16, 10}, {21, 9}, {64, 27}};
	public static int[][] AVATAR_ASPECT_RATIOS = {{1, 1}};
	public static int[] STEAM_AVATAR_SIZE = {184, 184};
	public static String[] IMAGE_FORMATS = {"jpg", "jpeg", "png"};
	
	public static String WALLPAPER_DIRECTORY_NAME = "Wallpapers";
	public static String AVATAR_DIRECTORY_NAME = "Avatars";
	public static String STEAM_AVATARS_DIRECTORY_NAME = "Steam Avatars (184x184)";
	
	public static void main(String[] args) {
		String folder = args[0]; // "C://Users//Connor Cummings//Pictures//TestFolder"
		
		File dir = new File(folder);
		if(dir.exists()) {
			String[] childNames = dir.list();
			if(childNames != null) {
				System.out.println("Organzing pictures at path: " + folder);
				
				// Create or find directories
				File wallpaperDir = createDirectory(folder + File.separator + WALLPAPER_DIRECTORY_NAME);
				File avatarDir = createDirectory(folder + File.separator + AVATAR_DIRECTORY_NAME);
				File steamAvatarsDir = createDirectory(avatarDir.getAbsolutePath() + File.separator + STEAM_AVATARS_DIRECTORY_NAME);
				
				sorting:
				for(int i = 0; i < childNames.length; i++) {
					File found = new File(folder + File.separator + childNames[i]);
					System.out.println("(" + getPercent(i+1, childNames.length) + "%) " + found.getName());
					// Check if image and supported format
					if(isSupportedImageFormat(found, IMAGE_FORMATS)) {
						Picture picture = new Picture(found);
						// Check resolution
						for(int[] aspectRatio: WALLPAPER_ASPECT_RATIOS) {
							if(picture.isAspectRatio(aspectRatio)) {
								File aspectRatioDir = createDirectory(wallpaperDir.getAbsolutePath() + File.separator + aspectRatio[0] + "x" + aspectRatio[1]);
								picture.copyTo(aspectRatioDir);
								continue sorting;
							}
						}
						for(int[] aspectRatio: AVATAR_ASPECT_RATIOS) {
							if(picture.isAspectRatio(aspectRatio)) {
								if(picture.isSize(STEAM_AVATAR_SIZE[0], STEAM_AVATAR_SIZE[1])) {
									picture.copyTo(steamAvatarsDir);
									continue sorting;
								}
								picture.copyTo(avatarDir);
								continue sorting;
							}
						}
					}
				}
			} else {
				System.out.println("File at filepath is not a directory.");
			}
		} else {
			System.out.println("File at filepath does not exist.");
		}
	}
	
	private static File createDirectory(String path) {
		File file =  new File(path);
		if(file.exists()) {
			if(file.isDirectory()) {
				return file;
			}
		} else {
			System.out.println("Directory at " + path + " does not exist, creating it...");
			if(file.mkdirs()) {
				return file;
			}
		}
		return null;
	}
	
	private static boolean isSupportedImageFormat(File file, String[] formats) {
		if(file.exists() && !file.isDirectory()) {
			String path = file.getAbsolutePath();
			for(String format: formats) {
				if(path.endsWith(format)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getPercent(int x, int max) {
		return (x*100/max*100)/100;
	}

}
