package files.unzipping;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class XmlZipTest {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String zipFilePath = ("C:" + File.separator + "Users" + File.separator + "SvetlanaNikonova" +
                File.separator + "Downloads" + File.separator + "SNC_AOD_01102023_061442.xml.zip");
        String destDir = ("C:" + File.separator + "Users" + File.separator + "SvetlanaNikonova" +
                File.separator + "Downloads" + File.separator + "output");
        String filePath = ("C:" + File.separator + "Users" + File.separator + "SvetlanaNikonova" +
                File.separator + "Downloads" + File.separator + "output" + File.separator + "SNC_AOD_01102023_061442.xml");


        unzip(zipFilePath, destDir);

        File file = new File(filePath);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println(doc.getDocumentElement().getNodeName());


        NodeList nodeList = doc.getElementsByTagName("PaidAmount");
        iterable(nodeList).forEach(node -> {
            try {
                double sum = Double.parseDouble(node.getTextContent());
                System.out.println("PaidAmount sum: " + sum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }


    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }


    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());

                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}