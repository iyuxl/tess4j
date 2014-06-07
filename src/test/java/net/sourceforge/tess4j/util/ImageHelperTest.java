package net.sourceforge.tess4j.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.IIOImage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class ImageHelperTest {
    private final String testResourcesDataPath = "src/test/resources/test-data";
    String filename = String.format("%s/%s", this.testResourcesDataPath, "eurotext.png");
    File imageFile = new File(filename);
    
    public ImageHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getScaledInstance method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testGetScaledInstance_3args() {
        System.out.println("getScaledInstance");
        BufferedImage image = null;
        int targetWidth = 0;
        int targetHeight = 0;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.getScaledInstance(image, targetWidth, targetHeight);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScaledInstance method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testGetScaledInstance_IIOImage_float() {
        System.out.println("getScaledInstance");
        IIOImage iioSource = null;
        float scale = 0.0F;
        IIOImage expResult = null;
        IIOImage result = ImageHelper.getScaledInstance(iioSource, scale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubImage method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testGetSubImage() {
        System.out.println("getSubImage");
        BufferedImage image = null;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.getSubImage(image, x, y, width, height);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertImageToBinary method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testConvertImageToBinary() {
        System.out.println("convertImageToBinary");
        BufferedImage image = null;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.convertImageToBinary(image);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertImageToGrayscale method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testConvertImageToGrayscale() {
        System.out.println("convertImageToGrayscale");
        BufferedImage image = null;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.convertImageToGrayscale(image);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invertImageColor method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testInvertImageColor() {
        System.out.println("invertImageColor");
        BufferedImage image = null;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.invertImageColor(image);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClipboardImage method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testGetClipboardImage() {
        System.out.println("getClipboardImage");
        Image expResult = null;
        Image result = ImageHelper.getClipboardImage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cloneImage method, of class ImageHelper.
     */
    @Test
    @Ignore("not ready yet")
    public void testCloneImage() {
        System.out.println("cloneImage");
        BufferedImage bi = null;
        BufferedImage expResult = null;
        BufferedImage result = ImageHelper.cloneImage(bi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
