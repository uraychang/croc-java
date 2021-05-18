import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import idv.ray.croc.client.Options;

public class FileTransferTester {
	public static void main(String[] args) throws IOException {
		String s = "1@2@3";
		System.out.println(s.split("@")[2]);
	}
}
