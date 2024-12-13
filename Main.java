import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;


//1
public class FileToUpperCase {
    public static void main(String[] args) {
        try (BufferedReader read = new BufferedReader(new FileReader("file1.txt"));
             BufferedWriter write = new BufferedWriter(new FileWriter("file2.txt"))) {

            String line;
            while ((line = read.readLine()) != null) {
                write.write(line.toUpperCase());
                write.newLine();
            }

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}


        //2
        public interface TextProcessor{
            String process(String text);
        }

        public static class SimpleTextProcessor implements TextProcessor {

            public String process(String text) {
                return text;
            }
        }

        public static class UpperCaseDecorator implements TextProcessor {

            private TextProcessor textProcessor;

            public UpperCaseDecorator(TextProcessor textProcessor) {
                this.textProcessor = textProcessor;
            }

            @Override
            public String process(String text) {
                return textProcessor.process(text).toUpperCase();
            }
        }

        public static class TrimDecorator implements TextProcessor {
            private TextProcessor textProcessor;

            public TrimDecorator(TextProcessor textProcessor) {
                this.textProcessor = textProcessor;
            }

            @Override
            public String process(String text) {
                return textProcessor.process(text).trim();
            }
        }

        public static class ReplaceDecorator implements TextProcessor {
            private TextProcessor textProcessor;

            public ReplaceDecorator(TextProcessor textProcessor) {
                this.textProcessor = textProcessor;
            }

            @Override
            public String process(String text) {
                return textProcessor.process(text).replace(" ", "_");
            }
        }

        public static void main(String[] args) {
            TextProcessor processor = new ReplaceDecorator(new UpperCaseDecorator(new TrimDecorator(new SimpleTextProcessor())));
            String result = processor.process(" Hello world ");
            System.out.println(result); // Вывод: HELLO_WORLD
        }


//3

        public static void main2(String[] args) {
            long startTime, endTime;

            startTime = System.currentTimeMillis();
            try (BufferedReader reader = new BufferedReader(new FileReader("file1.txt"));
                 BufferedWriter writer = new BufferedWriter(new FileWriter("file2io.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            endTime = System.currentTimeMillis();
            System.out.println("Время выполнения IO: " + (endTime - startTime) + " мс");

            startTime = System.currentTimeMillis();
            try (FileChannel inChannel = FileChannel.open(Paths.get("file1.txt"));
                 FileChannel outChannel = FileChannel.open(Paths.get("file2nio.txt"))) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (inChannel.read(buffer) > 0) {
                    buffer.flip();
                    outChannel.write(buffer);
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            endTime = System.currentTimeMillis();
            System.out.println("Время выполнения NIO: " + (endTime - startTime) + " мс");
        }


        //4
        public class FileCopyProgram {
            public static void main(String[] args) {
                String init = "initial.txt";
                String dest = "destination.txt";

                try {
                    copyFile(init, dest);
                    System.out.println("Copied successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }

            public static void copyFile(String init, String dest) throws IOException {
                try (FileChannel initChannel = new FileInputStream(init).getChannel();
                     FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
                    initChannel.transferTo(0, initChannel.size(), destChannel);
                }
            }
        }