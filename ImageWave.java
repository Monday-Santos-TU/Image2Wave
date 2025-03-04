import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ImageWave {
	private BufferedImage image, ogBufferedImage;
    private String inPath;
	private int skew;
	private final double TWOPI = 2 * Math.PI;

	public ImageWave(String inPath) {
        this.inPath = inPath;

		init();
	}

	private void init() {
        try {
			image = ImageIO.read(new File(inPath));
			ogBufferedImage = ImageIO.read(new File(inPath));
		} catch (Exception e) {
			System.err.println(e);
		}
	}

    public void start() {
		skew = 0;
		int ones, tens, huns, thos;
		File imgFile;
		for(skew = 0; skew < image.getHeight(); skew+=5) {
			march();

			ones = skew % 10;
			tens = (int)(skew % 100) / 10;
			huns = (int)(skew % 1000) / 100;
			thos = (int)(skew % 10000) / 1000;

			imgFile = new File("imgs/" + thos + huns + tens + ones + ".jpg");
			try {
				imgFile.createNewFile();
				ImageIO.write(image, "jpg", imgFile);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
    }

	private void march() {
		int w = ogBufferedImage.getWidth();
		int h = ogBufferedImage.getHeight();
		int pixel;
        int[] rgbArray = new int[w];
		int y;

		for(int j = 0; j < h; j++) {
			for(int i = 0; i < w; i++) {
				if(j > 0) {
					image.setRGB(i, j - 1, rgbArray[i]);
				}
				pixel = ogBufferedImage.getRGB(i, j);
                rgbArray[i] = pixel;
			}
			y = func(j + skew);
			shift(rgbArray, y);
		}

		for(int i = 0; i < w; i++) {
			image.setRGB(i, h - 1, rgbArray[i]);
		}
	}

	private int func(int x) {
		double b = ogBufferedImage.getHeight();
		double a = ogBufferedImage.getWidth() / 4;
		
		return (int) Math.floor(a * Math.sin(TWOPI / b * x));
	}

	private void shift(int[] A, int k) {
		if(k != 0) {
			int end = A.length - Math.abs(k);

			int[] t = new int[Math.abs(k)];
			int[] o = new int[end];

			if(k > 0) {
				for(int i = 0; i < t.length; i++) {
					t[i] = A[end + i];
				}

				for(int i = 0; i < end; i++) {
					o[i] = A[i];
				}

				for(int i = 0; i < t.length; i++) {
					A[i] = t[i];
				}

				for(int i = 0; i < o.length; i++) {
					A[i + k] = o[i];
				}
			} else if(k < 0) {
				k = Math.abs(k);

				for(int i = 0; i < t.length; i++) {
					t[i] = A[i];
				}

				for(int i = 0; i < end; i++) {
					o[i] = A[i + k];
				}

				for(int i = 0; i < t.length; i++) {
					A[i + end] = t[i];
				}

				for(int i = 0; i < o.length; i++) {
					A[i] = o[i];
				}
			}
		}
	}
	public static void main(String args[]) {
		ImageWave i = new ImageWave(args[0]);

		i.start();
	}
}
