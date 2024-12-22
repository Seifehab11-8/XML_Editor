package xml_editor;

public class Main  {

    public static void main(String[] args) {
    	Compression c1 = new Compression();
        String inputFilePath = null;
        String outputFilePath = null;
        // Parse the command-line arguments
        if (!args[0].equals("compress")) {
            System.out.println("Unknown command. Only 'compress' is supported.");
            return;
        }
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-i") && i + 1 < args.length) {
                inputFilePath = args[i + 1];
            } else if (args[i].equals("-o") && i + 1 < args.length) {
                outputFilePath = args[i + 1];
            }
        }

		if(inputFilePath.contains("xml") && !inputFilePath.contains("json")) {
			c1.setOutputPath(outputFilePath);
			c1.compressXML(inputFilePath);
		}
		else {
			System.out.println("JSON compression");
			c1.setOutputPath(outputFilePath);
			c1.compressJSON(inputFilePath);
		}
		System.out.println("File has been compressed successfully.");
    }
}